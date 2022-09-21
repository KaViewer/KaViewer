package com.koy.kaviewer.common.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.function.BiFunction;

public interface ConsumerService {
    List<ConsumerRecord<String, String>> fetchMessage(String topic, int partition, int size, Integer offset, BiFunction<byte[], String, String> keyDeserializer, BiFunction<byte[], String, String> valDeserializer);
}
