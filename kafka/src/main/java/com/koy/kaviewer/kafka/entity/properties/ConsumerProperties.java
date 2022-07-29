package com.koy.kaviewer.kafka.entity.properties;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerProperties extends Properties {
    private static final AtomicInteger index = new AtomicInteger(0);
    private static final String CLIENT_ID_PREFIX = "KaViewer::Consumer";
    // string / byte
    private String KeyDeserializer = "string";
    private String ValDeserializer = "string";
    private Integer maxPollRecords = 100;
    private String autoOffsetReset = "earliest";
    private String clientId;
    public KafkaProperties kafkaProperties;

    public ConsumerProperties(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public ConsumerProperties buildConsumerProperties() {
        final int idx = index.getAndIncrement();
        this.putAll(this.kafkaProperties);
        this.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        this.put(ConsumerConfig.GROUP_ID_CONFIG, generateClientId("Group", idx));
        this.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        this.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        this.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
        this.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
        this.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        this.put(ConsumerConfig.CLIENT_ID_CONFIG, generateClientId("Client", idx));
        return this;

    }

    public String getKeyDeserializer() {
        return KeyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        KeyDeserializer = keyDeserializer;
    }

    public String getValDeserializer() {
        return ValDeserializer;
    }

    public void setValDeserializer(String valDeserializer) {
        ValDeserializer = valDeserializer;
    }

    public Integer getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(Integer maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String generateClientId(String dot, int idx) {
        this.clientId = dot + "::" + CLIENT_ID_PREFIX + this.kafkaProperties.getClusterName() + "-" + idx;
        return clientId;
    }
}
