package com.koy.kaviewer.app.ipc;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;


@Service
public class ConsumerService implements com.koy.kaviewer.kafka.ipc.ConsumerService {

    @Override
    public List<ConsumerRecord<String, String>> fetchMessage(String topic, int partition, int size, String sorted, BiFunction<byte[], String, String> keyDeserializer, BiFunction<byte[], String, String> valDeserializer) {
        final com.koy.kaviewer.kafka.ipc.ConsumerService consumerService
                = KafkaApplication.getKafkaApplicationBean(com.koy.kaviewer.kafka.ipc.ConsumerService.class);
        return consumerService.fetchMessage(topic, partition, size, sorted, keyDeserializer, valDeserializer);
    }
}
