package com.koy.kaviewer.kafka.ipc.impl;

import com.koy.kaviewer.common.ipc.ProducerService;
import com.koy.kaviewer.kafka.service.KafkaProducerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final KafkaProducerFactory kafkaProducerFactory;

    @Override
    public boolean publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val) {
        final List<Header> recordHeaders = Optional.ofNullable(headers).orElseGet(Map::of).entrySet().stream().filter(entry -> {
            final String k = entry.getKey();
            final Object v = entry.getValue();
            return Objects.nonNull(k) && Objects.nonNull(v);
        }).map(entry -> {
            String k = entry.getKey();
            byte[] valBytes;
            final Object value = entry.getValue();
            if (value instanceof String) {
                valBytes = ((String) value).getBytes(StandardCharsets.UTF_8);
            } else if (value instanceof byte[]) {
                valBytes = (byte[]) value;
            } else {
                valBytes = String.valueOf(value).getBytes(StandardCharsets.UTF_8);
            }
            return new RecordHeader(k, valBytes);
        }).collect(Collectors.toList());

        kafkaProducerFactory.publish(topic, partition, recordHeaders, key, val);
        return true;
    }
}
