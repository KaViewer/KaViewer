package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.entity.properties.ProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

// https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
@Repository
public class KafkaProducerFactory {

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

    public void publish(String topic, int partition, List<Header> recordHeaders, byte[] key, byte[] val) {
        createProducer();

        final ProducerRecord<byte[], byte[]> record =
                new ProducerRecord<>(topic, partition, new Timestamp(System.currentTimeMillis()).getTime(), key, val, recordHeaders);
        this.kafkaProducer4Byte.send(record);
    }

}

