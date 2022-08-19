package com.koy.kaviewer.web.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.record.TimestampType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@ToString
@NoArgsConstructor
public class MessageRecord<K, V> {
    private String topic;
    private int partition;
    private long offset;
    private long timestamp;
    private TimestampType timestampType;
    private int serializedKeySize;
    private int serializedValueSize;
    private Map<String, Object> headers;
    private K key;
    private V value;
    private static final Function<byte[], String> headerConverter = (byteHeader) -> {
        if (Objects.isNull(byteHeader)) {
            return String.valueOf((Object) null);
        }
        return new String(byteHeader);
    };

    public MessageRecord(String topic, int partition, long offset, long timestamp, TimestampType timestampType, int serializedKeySize, int serializedValueSize, Headers headers, K key, V value) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
        this.timestampType = timestampType;
        this.serializedKeySize = serializedKeySize;
        this.serializedValueSize = serializedValueSize;

        this.headers = Arrays.stream(headers.toArray()).collect(Collectors.toMap(Header::key, h -> headerConverter.apply(h.value()), (v1, v2) -> v2));
        this.key = key;
        this.value = value;
    }
}
