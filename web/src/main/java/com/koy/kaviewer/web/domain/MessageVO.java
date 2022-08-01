package com.koy.kaviewer.web.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

// org.apache.kafka.clients.producer.ProducerRecord
public class MessageVO {

    private String topic;
    private int partition;
    private List<HeaderVO> headers = List.of();
    private String key;
    private String value;

    public boolean inValid() {
        return StringUtils.isEmpty(topic) || StringUtils.isEmpty(value);
    }

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

    public List<HeaderVO> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderVO> headers) {
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
