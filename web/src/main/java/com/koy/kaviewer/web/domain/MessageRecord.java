package com.koy.kaviewer.web.domain;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.record.TimestampType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            return String.valueOf((Object)null);
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


    @Override
    public String toString() {
        return "MessageRecord{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                ", timestamp=" + timestamp +
                ", timestampType=" + timestampType +
                ", serializedKeySize=" + serializedKeySize +
                ", serializedValueSize=" + serializedValueSize +
                ", headers=" + headers +
                ", key=" + key +
                ", value=" + value +
                '}';
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestampType(TimestampType timestampType) {
        this.timestampType = timestampType;
    }

    public void setSerializedKeySize(int serializedKeySize) {
        this.serializedKeySize = serializedKeySize;
    }

    public void setSerializedValueSize(int serializedValueSize) {
        this.serializedValueSize = serializedValueSize;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TimestampType getTimestampType() {
        return timestampType;
    }

    public int getSerializedKeySize() {
        return serializedKeySize;
    }

    public int getSerializedValueSize() {
        return serializedValueSize;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


}
