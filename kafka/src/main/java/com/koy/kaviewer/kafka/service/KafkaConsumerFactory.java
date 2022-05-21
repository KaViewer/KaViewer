package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.core.KafkaProperties;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class KafkaConsumerFactory {

    @Autowired
    private KafkaClientWrapper kafkaClientWrapper;
    private KafkaConsumer<byte[], byte[]> kafkaConsumer4Byte;
    private KafkaConsumer<String, String> kafkaConsumer4String;
    private KafkaProperties.ConsumerProperties consumerProperties;

    public synchronized void refresh() {
        buildTopicsMeta();
    }

    private synchronized void createConsumer() {
        createConsumer4Byte();
        createConsumer4String();
    }

    private synchronized KafkaConsumer<byte[], byte[]> createConsumer4Byte() {
        if (Objects.nonNull(kafkaConsumer4Byte)) {
            return this.kafkaConsumer4Byte;
        }
        final KafkaProperties kafkaProperties = kafkaClientWrapper.getKafkaProperties();
        this.consumerProperties = kafkaProperties.getConsumer();
        this.kafkaConsumer4Byte = new KafkaConsumer<>(consumerProperties);
        return this.kafkaConsumer4Byte;
    }

    private synchronized KafkaConsumer<String, String> createConsumer4String() {
        if (Objects.nonNull(kafkaConsumer4String)) {
            return this.kafkaConsumer4String;
        }
        final KafkaProperties kafkaProperties = kafkaClientWrapper.getKafkaProperties();
        this.consumerProperties = kafkaProperties.getConsumer();
        this.kafkaConsumer4String = new KafkaConsumer<>(consumerProperties);
        return this.kafkaConsumer4String;
    }

    public List<TopicMetaVO> buildTopicsMeta() {
        createConsumer4Byte();
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

    public List<ConsumerRecord<byte[], byte[]>> fetchMessage4Byte(String topic, int partition, int size, String sorted) {
        createConsumer();
        final TopicPartition topicPartition = new TopicPartition(topic, partition);
        this.kafkaConsumer4Byte.assign(Set.of(topicPartition));
        seek(this.kafkaConsumer4Byte, size, topicPartition);
        List<ConsumerRecord<byte[], byte[]>> records;

        records = kafkaConsumer4Byte.poll(Duration.ofSeconds(30)).records(topicPartition);
        return records
                .stream()
                .map(rec -> new ConsumerRecord<byte[], byte[]>(
                        rec.topic(),
                        rec.partition(),
                        rec.offset(),
                        rec.timestamp(),
                        rec.timestampType(),
                        rec.checksum(),
                        rec.serializedKeySize(),
                        rec.serializedValueSize(),
                        rec.key(),
                        rec.value(),
                        rec.headers(),
                        rec.leaderEpoch())
                )
                .collect(Collectors.toList());
    }

    public List<ConsumerRecord<String, String>> fetchMessage4String(String topic, int partition, int size, String sorted) {
        createConsumer();
        final TopicPartition topicPartition = new TopicPartition(topic, partition);
        this.kafkaConsumer4String.assign(Set.of(topicPartition));
        List<ConsumerRecord<String, String>> records = List.of();
        seek(this.kafkaConsumer4String, size, topicPartition);
        records = kafkaConsumer4String.poll(Duration.ofSeconds(30)).records(topicPartition);
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
                        rec.key(),
                        rec.value(),
                        rec.headers(),
                        rec.leaderEpoch())
                )
                .collect(Collectors.toList());
    }

    private void seek(KafkaConsumer kafkaConsumer, int size, TopicPartition topicPartition) {
        final Long earliestOffset = (Long) kafkaConsumer.beginningOffsets(Set.of(topicPartition)).get(topicPartition);
        final long latestOffset = (long) kafkaConsumer.endOffsets(Set.of(topicPartition)).get(topicPartition);
        // poll all
        if (size > latestOffset) {
            kafkaConsumer.seek(topicPartition, earliestOffset);
        } else {
            kafkaConsumer.seek(topicPartition, latestOffset - size);
        }
    }
}

