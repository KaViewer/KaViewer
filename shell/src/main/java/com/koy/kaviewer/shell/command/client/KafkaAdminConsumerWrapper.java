package com.koy.kaviewer.shell.command.client;

import com.google.common.collect.ImmutableList;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaAdminConsumerWrapper {
    private AdminClientCfg config;
    private KafkaConsumer<String, String> kafkaConsumer;
    private static final Properties defaultProps = new Properties();

    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    static {
        defaultProps.put(BOOTSTRAP_SERVERS, "localhost:9092");
        defaultProps.put("group.id", "test");
        defaultProps.put("enable.auto.commit", "false");
        defaultProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        defaultProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    }

    public void config(AdminClientCfg config) {
        this.config = config;
    }

    public void create(String topic) {
        create(topic, 0);
    }

    public void create(String topic, int partition) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        Properties properties = new Properties(defaultProps);
//        properties.put(Kafka)
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.assign(ImmutableList.of(topicPartition));


    }
}
