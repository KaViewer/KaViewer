package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.common.entity.TopicMetaVO;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class KafkaConsumerFactory {

    private BlockingQueue<KafkaConsumer<byte[], byte[]>> kafkaConsumers;
    private final Lock kafkaConsumersLock = new ReentrantLock();
    private KafkaProperties kafkaProperties;

    final void createConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;

        final Integer consumerWorkerSize = this.kafkaProperties.getConsumerWorkerSize();
        log.info("Config consumerWorkerSize is: [{}]", consumerWorkerSize);
        kafkaConsumers = new ArrayBlockingQueue<>(consumerWorkerSize);
        doCreateConsumer();
    }

    private void doCreateConsumer() {
        kafkaConsumersLock.lock();
        try {
            if (kafkaConsumers.size() < kafkaProperties.getConsumerWorkerSize()) {
                final KafkaConsumer<byte[], byte[]> kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProperties());
                kafkaConsumers.add(kafkaConsumer);
            }
        } finally {
            kafkaConsumersLock.unlock();
        }
    }

    // TODO check and remove hanging a long time consumer
    private void returnBackConsumer(KafkaConsumer<byte[], byte[]> kafkaConsumer) {
        if (Objects.isNull(kafkaConsumer)) {
            return;
        }
        kafkaConsumersLock.lock();
        try {
            if (kafkaConsumers.size() >= kafkaProperties.getConsumerWorkerSize()) {
                kafkaConsumer.close();
            } else {
                kafkaConsumers.add(kafkaConsumer);
            }
        } finally {
            kafkaConsumersLock.unlock();
        }
    }

    private <V> V exec(Function<KafkaConsumer<byte[], byte[]>, V> runnable) {
        V result;
        kafkaConsumersLock.lock();
        try {
            doCreateConsumer();
            final KafkaConsumer<byte[], byte[]> consumer = kafkaConsumers.poll(30L, TimeUnit.SECONDS);
            result = runnable.apply(consumer);
            returnBackConsumer(consumer);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        } finally {
            kafkaConsumersLock.unlock();
        }

        return result;
    }

    public List<TopicMetaVO> buildTopicsMeta() {
        return buildTopicsMeta(false);
    }

    public List<TopicMetaVO> buildTopicsMeta(boolean assign) {
        return exec((kafkaConsumer) -> {
            if (Objects.isNull(kafkaConsumer)) {
                log.info("kafkaConsumer is unavailable");
                return List.of();
            }

            final Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics(Duration.ofSeconds(30));
            final List<TopicMetaVO> topicMetaVOS = new ArrayList<>(topics.size());
            topics.forEach((topic, pt) -> {
                final List<TopicPartition> tps = pt.stream()
                        .map(p -> new TopicPartition(topic, p.partition())).collect(Collectors.toList());
                if (assign) {
                    kafkaConsumer.assign(tps);
                }
                topicMetaVOS.add(new TopicMetaVO(topic, pt, tps));
            });
            return topicMetaVOS;
        });
    }

    public List<ConsumerRecord<String, String>> fetchMessage(String topic, int partition, int size, Integer offset,
                                                             BiFunction<byte[], String, String> keyDeserializer,
                                                             BiFunction<byte[], String, String> valDeserializer) {

        log.info("Receive fetchMessage params, topic:[{}], partition:[{}], size:[{}]", topic, partition, size);
        // max size is 200
        if (size > 1000) size = 500;

        final int finalSize = size;
        return exec((kafkaConsumer -> {
            if (Objects.isNull(kafkaConsumer)) {
                log.info("kafkaConsumer is unavailable");
                return List.of();
            }

            Set<TopicPartition> topicPartitions;
            final boolean fetchAll = partition == -1;
            // fetch All
            if (fetchAll) {
                List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor(topic, Duration.ofSeconds(30L));
                topicPartitions = partitionInfos.stream()
                        .map(pt -> new TopicPartition(topic, pt.partition()))
                        .collect(Collectors.toSet());
                // fetch single partition
            } else {
                topicPartitions = Set.of(new TopicPartition(topic, partition));
            }


            final Map<TopicPartition, Long> latestOffsets = kafkaConsumer.endOffsets(topicPartitions);
            int maxOffset = latestOffsets.values().stream().mapToInt(Long::intValue).max().orElse(0);

            if (maxOffset == 0) {
                return List.of();
            }

            if (Objects.nonNull(offset) && offset > maxOffset) {
                return List.of();
            }

            final Map<TopicPartition, Long> topicPartitionLatestOffsetTable = new HashMap<>(topicPartitions.size());

            kafkaConsumer.assign(topicPartitions);
            final List<TopicPartition> availableTopicPartitions = new ArrayList<>(topicPartitions.size());

            for (TopicPartition topicPartition : topicPartitions) {
                final long latestOffset = latestOffsets.get(topicPartition);
                topicPartitionLatestOffsetTable.put(topicPartition, latestOffset);
                if (Objects.nonNull(offset)) {
                    if (offset > latestOffset) {
                        // offset is larger, skip
                        topicPartitionLatestOffsetTable.remove(topicPartition);
                        log.info("Offset:[{}] is larger than latestOffset:[{}], skip on partition:[{}]", offset, latestOffset, topicPartition.partition());
                    } else {
                        availableTopicPartitions.add(topicPartition);
                        kafkaConsumer.seek(topicPartition, offset);
                    }
                } else {
                    availableTopicPartitions.add(topicPartition);
                    kafkaConsumer.seek(topicPartition, Math.max(0, latestOffset - finalSize));
                }
            }

            List<ConsumerRecord<byte[], byte[]>> records = new ArrayList<>(finalSize);

            boolean fetch = true;
            while ((records.size() < finalSize * availableTopicPartitions.size()) && fetch) {
                final var recordsOrigin = kafkaConsumer.poll(Duration.ofSeconds(30));
                for (TopicPartition tp : availableTopicPartitions) {
                    final List<ConsumerRecord<byte[], byte[]>> recs = recordsOrigin.records(tp);
                    if (recs.isEmpty()) {
                        continue;
                    }
                    records.addAll(recs);
                    final long currentFetchedLatestOffset = recs.get(recs.size() - 1).offset();
                    var currentPartitionLatestOffset = topicPartitionLatestOffsetTable.get(tp);
                    // fetch to the end of current partition
                    if (currentFetchedLatestOffset >= (currentPartitionLatestOffset - 1)) {
                        topicPartitionLatestOffsetTable.remove(tp);
                    }

                    if (topicPartitionLatestOffsetTable.isEmpty()) {
                        fetch = false;
                        break;
                    }
                }
            }

            log.info("Fetch records size:[{}]", records.size());
            // get marched records in size
            if (Objects.isNull(offset)) {
                records = records.subList(Math.max(0, records.size() - finalSize), records.size());
            } else {
                records = records.subList(0, Math.min(finalSize, records.size()));
            }

            log.info("Return records size:[{}]", records.size());

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

