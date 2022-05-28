package com.koy.kaviewer.kafka.ipc.impl;

import com.koy.kaviewer.kafka.ipc.ProducerService;
import com.koy.kaviewer.kafka.service.KafkaProducerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    KafkaProducerFactory kafkaProducerFactory;

    @Override
    public boolean publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val) {
        kafkaProducerFactory.publish(topic, partition, headers, key, val);
        return true;
    }
}
