package com.koy.kaviewer.kafka.impl;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.common.service.ConsumerService;
import com.koy.kaviewer.kafka.service.KafkaConsumerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;


@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final KafkaClientWrapper kafkaClientWrapper;
    private final KafkaConsumerFactory kafkaConsumerFactory;

    private static final BiFunction<byte[], String, String> stringDeserializer = (data, encoding) -> {
        try {
            if (Objects.isNull(data))
                return null;
            else
                return new String(data, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding " + encoding);
        }
    };

    private static final BiFunction<byte[], String, String> byteDeserializer = (data, encoding) -> Arrays.toString(data);

    private static final Map<String, BiFunction<byte[], String, String>> deserializers = Map.of("string", stringDeserializer, "byte", byteDeserializer);

    @Override
    public List<ConsumerRecord<String, String>> fetchMessage(String topic, int partition, int size, String sorted, BiFunction<byte[], String, String> keyDeserializer, BiFunction<byte[], String, String> valDeserializer) {
        return kafkaConsumerFactory.fetchMessage(topic, partition, size, sorted, keyDeserializer, valDeserializer);
    }
}
