package com.koy.kaviewer.kafka.ipc;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface ConsumerService {
    List<ConsumerRecord<String, String >> fetchMessage(String topic, int partition, int size, String sorted);
}
