package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.core.KafkaProperties;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Repository
public class KafkaConsumerFactory {

    @Autowired
    private KafkaClientWrapper kafkaClientWrapper;
    private KafkaConsumer<byte[], byte[]> kafkaConsumer4Byte;
    private KafkaProperties.ConsumerProperties consumerProperties;
    private KafkaProperties kafkaProperties;

    private synchronized KafkaConsumer<byte[], byte[]> createConsumer() {
        if (Objects.nonNull(kafkaConsumer4Byte)) {
            return this.kafkaConsumer4Byte;
        }
        this.kafkaProperties = kafkaClientWrapper.getKafkaProperties();

        this.consumerProperties = kafkaProperties.getConsumer();
        this.kafkaConsumer4Byte = new KafkaConsumer<>(consumerProperties);
        return this.kafkaConsumer4Byte;
    }

    public List<TopicMetaVO> buildTopicsMeta() {
        createConsumer();
        final Map<String, List<PartitionInfo>> topics = this.kafkaConsumer4Byte.listTopics();
        final List<TopicMetaVO> topicMetaVOS = new ArrayList<>(topics.size());
        topics.forEach((topic, pt) -> {
            final List<TopicPartition> tps = pt.stream()
                    .map(p -> new TopicPartition(topic, p.partition())).collect(Collectors.toList());
            this.kafkaConsumer4Byte.assign(tps);
            topicMetaVOS.add(new TopicMetaVO(topic, pt, tps));
        });
        return topicMetaVOS;
    }

    public List<ConsumerRecord<String, String>> fetchMessage(String topic, int partition, int size, String sorted,
                                                             BiFunction<byte[], String, String> keyDeserializer,
                                                             BiFunction<byte[], String, String> valDeserializer) {
        createConsumer();
        final TopicPartition topicPartition = new TopicPartition(topic, partition);
        this.kafkaConsumer4Byte.assign(Set.of(topicPartition));
        seek(this.kafkaConsumer4Byte, size, topicPartition);
        List<ConsumerRecord<byte[], byte[]>> records;

        records = kafkaConsumer4Byte.poll(Duration.ofSeconds(30)).records(topicPartition);
        return records
                .stream()
                .map(rec -> new ConsumerRecord<String, String>(
                        rec.topic(),
                        rec.partition(),
                        rec.offset(),
                        rec.timestamp(),
                        rec.timestampType(),
                        rec.checksum(),
                        rec.serializedKeySize(),
                        rec.serializedValueSize(),
                        keyDeserializer.apply(rec.key(), this.kafkaProperties.getEncoding()),
                        valDeserializer.apply(rec.value(), this.kafkaProperties.getEncoding()),
                        rec.headers(),
                        rec.leaderEpoch())
                )
                .collect(Collectors.toList());
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

