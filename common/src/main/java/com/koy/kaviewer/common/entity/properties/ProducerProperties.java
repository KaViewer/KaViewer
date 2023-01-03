package com.koy.kaviewer.common.entity.properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerProperties extends Properties {
    private static final AtomicInteger index = new AtomicInteger(0);
    private final String CLIENT_ID_PREFIX = "KaViewer::Consumer";
    private String clientId;
    public KafkaProperties kafkaProperties;

    public ProducerProperties(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public ProducerProperties buildProducerProperties() {
        this.putAll(kafkaProperties);
        this.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        this.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        this.put(ProducerConfig.CLIENT_ID_CONFIG, getClientId("Client", index.getAndIncrement()));
        return this;

    }

    private String getClientId(String dot, int idx) {
        this.clientId = dot + "::" + CLIENT_ID_PREFIX + this.kafkaProperties.getClusterName() + "-" + idx;
        return clientId;
    }

}
