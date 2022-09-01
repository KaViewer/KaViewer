package com.koy.kaviewer.web.service;

import com.koy.kaviewer.common.service.ConsumerService;
import com.koy.kaviewer.common.service.TopicService;
import com.koy.kaviewer.web.core.RequestContextManagement;
import com.koy.kaviewer.web.KaViewerWebApplication;
import com.koy.kaviewer.web.domain.MessageRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class ConsumerBizService {
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

    public List<MessageRecord<String, String>> fetch(String topic, int partition, int size, String sorted, String key, String val) {

        final TopicService topicService = KaViewerWebApplication.getBean(TopicService.class);
        final Set<String> topics = topicService.list(RequestContextManagement.getCluster());

        if (!topics.contains(topic)) {
            return List.of();
        }

        final ConsumerService consumerService = KaViewerWebApplication.getBean(ConsumerService.class);
        final BiFunction<byte[], String, String> keyDeserializer = deserializers.getOrDefault(key, stringDeserializer);
        final BiFunction<byte[], String, String> valDeserializer = deserializers.getOrDefault(val, stringDeserializer);

        final List<ConsumerRecord<String, String>> records = consumerService.fetchMessage(topic, partition, size, sorted, keyDeserializer, valDeserializer);
        return records.stream().map(rds ->
                new MessageRecord<String, String>(
                        rds.topic(),
                        rds.partition(),
                        rds.offset(),
                        rds.timestamp(),
                        rds.timestampType(),
                        rds.serializedKeySize(),
                        rds.serializedValueSize(),
                        rds.headers(),
                        rds.key(),
                        rds.value()
                )).collect(Collectors.toList());
    }

}
