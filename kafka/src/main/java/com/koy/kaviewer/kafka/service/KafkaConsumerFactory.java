package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.common.entity.TopicMetaVO;

import com.koy.kaviewer.common.entity.properties.ConsumerProperties;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class KafkaConsumerFactory {

    private BlockingQueue<KafkaConsumer<byte[], byte[]>> kafkaConsumers;
    private KafkaProperties kafkaProperties;

    final void createConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;

        final Integer consumerWorkerSize = this.kafkaProperties.getConsumerWorkerSize();
        log.info("Config consumerWorkerSize is: [{}]", consumerWorkerSize);
        kafkaConsumers = new ArrayBlockingQueue<>(consumerWorkerSize);

        // TODO lazy init consumers instead of create all size first
        for (int i = 0; i < consumerWorkerSize; i++) {
            final ConsumerProperties consumerProperties = kafkaProperties.getConsumer();
            final KafkaConsumer<byte[], byte[]> kafkaConsumer4Byte = new KafkaConsumer<>(consumerProperties);
            kafkaConsumers.add(kafkaConsumer4Byte);
        }
    }

    private <V> V exec(Function<KafkaConsumer<byte[], byte[]>, V> runnable) {
        V result = null;
        try {
            final KafkaConsumer<byte[], byte[]> consumer = kafkaConsumers.poll(30L, TimeUnit.SECONDS);
            result = runnable.apply(consumer);
            if (Objects.nonNull(consumer)) {
                kafkaConsumers.add(consumer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }

        return result;
    }

    public List<TopicMetaVO> buildTopicsMeta() {
        return exec((kafkaConsumer) -> {

            final Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics(Duration.ofSeconds(30));
            final List<TopicMetaVO> topicMetaVOS = new ArrayList<>(topics.size());
            topics.forEach((topic, pt) -> {
                final List<TopicPartition> tps = pt.stream()
                        .map(p -> new TopicPartition(topic, p.partition())).collect(Collectors.toList());
                kafkaConsumer.assign(tps);
                topicMetaVOS.add(new TopicMetaVO(topic, pt, tps));
            });
            return topicMetaVOS;
        });
    }

    public List<ConsumerRecord<String, String>> fetchMessage(String topic, int partition, int size, String sorted,
                                                             BiFunction<byte[], String, String> keyDeserializer,
                                                             BiFunction<byte[], String, String> valDeserializer) {

        log.info("Receive fetchMessage params, topic:[{}], partition:[{}], size:[{}]", topic, partition, size);
        // max size is 200
        if (size > 200) size = 200;

        int finalSize = size;
        return exec((kafkaConsumer -> {
            Set<TopicPartition> topicPartitions;
            // fetch All
            if (partition == -1) {
                List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor(topic, Duration.ofSeconds(30L));
                topicPartitions = partitionInfos.stream()
                        .map(pt -> new TopicPartition(topic, pt.partition()))
                        .collect(Collectors.toSet());
            } else {
                topicPartitions = Set.of(new TopicPartition(topic, partition));
            }
            kafkaConsumer.assign(topicPartitions);
            final Map<TopicPartition, Long> latestOffsets = kafkaConsumer.endOffsets(topicPartitions);

            topicPartitions.forEach(tp -> {
                final long latestOffset = latestOffsets.get(tp);
                kafkaConsumer.seek(tp, Math.max(0, latestOffset - finalSize));
            });
            List<ConsumerRecord<byte[], byte[]>> records = new ArrayList<>(finalSize);

            final var recordsOrigin = kafkaConsumer.poll(Duration.ofSeconds(30));

            for (TopicPartition tp : topicPartitions) {
                records.addAll(recordsOrigin.records(tp));
            }

            return records
                    .stream()
                    .map(rec -> new ConsumerRecord<>(
                            rec.topic(),
                            rec.partition(),
                            rec.offset(),
                            rec.timestamp(),
                            rec.timestampType(),
                            rec.serializedKeySize(),
                            rec.serializedValueSize(),
                            keyDeserializer.apply(rec.key(), this.kafkaProperties.getEncoding()),
                            valDeserializer.apply(rec.value(), this.kafkaProperties.getEncoding()),
                            rec.headers(),
                            rec.leaderEpoch())
                    )
                    .collect(Collectors.toList());

        }));
    }
}

