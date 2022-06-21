package com.koy.kaviewer.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.entity.properties.ProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

// https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
@Repository
public class KafkaProducerFactory {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaClientWrapper kafkaClientWrapper;
    private KafkaProducer<byte[], byte[]> kafkaProducer4Byte;
    private ProducerProperties producerProperties;
    private KafkaProperties kafkaProperties;

    private synchronized KafkaProducer<byte[], byte[]> createProducer() {
        if (Objects.nonNull(kafkaProducer4Byte)) {
            return this.kafkaProducer4Byte;
        }
        this.kafkaProperties = kafkaClientWrapper.getKafkaProperties();
        this.producerProperties = this.kafkaProperties.getProducer();

        kafkaProducer4Byte = new KafkaProducer<>(producerProperties);

        return this.kafkaProducer4Byte;
    }

    public void publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val) {
        createProducer();
        final List<Header> recordHeaders = Optional.ofNullable(headers).orElseGet(Map::of).entrySet().stream().filter(entry -> {
            final String k = entry.getKey();
            final Object v = entry.getValue();
            return Objects.nonNull(k) && Objects.nonNull(v);
        }).map(entry -> {
            try {
                String k = entry.getKey();
                final byte[] valBytes = objectMapper.writeValueAsBytes(entry.getValue());
                return new RecordHeader(k, valBytes);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        final ProducerRecord<byte[], byte[]> record =
                new ProducerRecord<>(topic, partition, new Timestamp(System.currentTimeMillis()).getTime(), key, val, recordHeaders);
        this.kafkaProducer4Byte.send(record);
    }

}

