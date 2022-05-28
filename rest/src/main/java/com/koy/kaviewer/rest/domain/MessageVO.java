package com.koy.kaviewer.rest.domain;

import com.google.common.collect.Maps;

import java.util.Map;

public class MessageVO {

    private String topic;
    private int partition;
    private Map<String, Object> headers = Maps.newHashMap();
    private String key;
    private String value;

    @Override
    public String toString() {
        return "MessageVO{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", headers=" + headers +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
