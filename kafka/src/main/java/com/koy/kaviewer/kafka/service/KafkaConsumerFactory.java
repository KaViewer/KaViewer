package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.entity.properties.ConsumerProperties;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
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
public class KafkaConsumerFactory {

    private BlockingQueue<KafkaConsumer<byte[], byte[]>> kafkaConsumers;
    private KafkaProperties kafkaProperties;

    final void createConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;

        final Integer consumerWorkerSize = this.kafkaProperties.getConsumerWorkerSize();
        kafkaConsumers = new ArrayBlockingQueue<>(consumerWorkerSize);

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
        }catch (Exception e){
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }

        return result;
    }

    public List<TopicMetaVO> buildTopicsMeta() {
        return exec((kafkaConsumer) -> {

            final Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics();
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
        // max size is 200
        if (size > 200) size = 200;

        final TopicPartition topicPartition = new TopicPartition(topic, partition);
        int finalSize = size;
        return exec((kafkaConsumer -> {
            kafkaConsumer.assign(Set.of(topicPartition));
            seek(kafkaConsumer, finalSize, topicPartition);
            List<ConsumerRecord<byte[], byte[]>> records;

            records = kafkaConsumer.poll(Duration.ofSeconds(30)).records(topicPartition);
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

    private void seek(KafkaConsumer<byte[], byte[]> kafkaConsumer, int size, TopicPartition topicPartition) {
        final Long earliestOffset = kafkaConsumer.beginningOffsets(Set.of(topicPartition)).get(topicPartition);
        final long latestOffset = kafkaConsumer.endOffsets(Set.of(topicPartition)).get(topicPartition);
        // poll all
        if (size > latestOffset) {
            kafkaConsumer.seek(topicPartition, earliestOffset);
        } else {
            kafkaConsumer.seek(topicPartition, latestOffset - size);
        }
    }
}

