package com.koy.kaviewer.kafka.ipc.impl;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.ipc.ConsumerService;
import com.koy.kaviewer.kafka.service.KafkaConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    KafkaClientWrapper kafkaClientWrapper;

    @Autowired
    KafkaConsumerFactory kafkaConsumerFactory;

    @Override
    public List<ConsumerRecord<String, String>> fetchMessageInString(String topic, int partition, int size, String sorted) {
        return null;
    }

    @Override
    public List<ConsumerRecord<byte[], byte[]>> fetchMessageInByte(String topic, int partition, int size, String sorted) {
        return null;
    }
}
