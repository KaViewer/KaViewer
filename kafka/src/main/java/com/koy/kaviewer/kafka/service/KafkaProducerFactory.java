package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

// https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
@Repository
public class KafkaProducerFactory {

    private final KafkaClientWrapper kafkaClientWrapper;
    private KafkaProducer<byte[], byte[]> kafkaProducer4Byte;

    public KafkaProducerFactory(KafkaClientWrapper kafkaClientWrapper) {
        this.kafkaClientWrapper = kafkaClientWrapper;
    }

    @PreDestroy
    public void destroy() {
        try {
            if (Objects.nonNull(kafkaProducer4Byte)) {
                kafkaProducer4Byte.close();
            }
        } catch (Exception ignore) {
        }
    }

    private synchronized KafkaProducer<byte[], byte[]> createProducer() {
        if (Objects.nonNull(kafkaProducer4Byte)) {
            return this.kafkaProducer4Byte;
        }
        var kafkaProperties = kafkaClientWrapper.getKafkaProperties();

        kafkaProducer4Byte = new KafkaProducer<>(kafkaProperties.getProducerProperties());

        return this.kafkaProducer4Byte;
    }

    public void publish(String topic, int partition, List<Header> recordHeaders, byte[] key, byte[] val) {
        createProducer();

        final ProducerRecord<byte[], byte[]> record =
                new ProducerRecord<>(topic, partition, new Timestamp(System.currentTimeMillis()).getTime(), key, val, recordHeaders);
        this.kafkaProducer4Byte.send(record);
    }

}

