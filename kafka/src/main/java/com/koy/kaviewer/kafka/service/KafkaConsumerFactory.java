package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.core.KafkaProperties;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class KafkaConsumerFactory {

    @Autowired
    private KafkaClientWrapper kafkaClientWrapper;
    private KafkaConsumer<byte[], byte[]> kafkaConsumer;

    private void createConsumer() {
        final KafkaProperties kafkaProperties = kafkaClientWrapper.getKafkaProperties();
        final KafkaProperties.ConsumerProperties consumerProperties = kafkaProperties.getConsumer();
        this.kafkaConsumer = new KafkaConsumer<>(consumerProperties);
    }

    public List<TopicMetaVO> buildTopicsMeta() {
        createConsumer();
        final Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics();
        final List<TopicMetaVO> topicMetaVOS = new ArrayList<>(topics.size());
        topics.forEach((topic, pt) -> {
            final List<TopicPartition> tps = pt.stream()
                    .map(p -> new TopicPartition(topic, p.partition())).collect(Collectors.toList());
            this.kafkaConsumer.assign(tps);
            topicMetaVOS.add(new TopicMetaVO(topic, pt, tps));
        });
        return topicMetaVOS;
    }

}
