package com.koy.kaviewer.kafka.ipc;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface ConsumerService {
    List<ConsumerRecord<String, String >> fetchMessageInString(String topic, int partition, int size, String sorted);
    List<ConsumerRecord<byte[], byte[] >> fetchMessageInByte(String topic, int partition, int size, String sorted);
}
