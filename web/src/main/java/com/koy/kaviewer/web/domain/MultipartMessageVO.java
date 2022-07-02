package com.koy.kaviewer.web.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MultipartMessageVO {

    private String topic;
    private int partition;
    private List<HeaderVO> headers = List.of();
    private MultipartFile key;
    private MultipartFile value;

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

    public MultipartFile getKey() {
        return key;
    }

    public void setKey(MultipartFile key) {
        this.key = key;
    }

    public MultipartFile getValue() {
        return value;
    }

    public void setValue(MultipartFile value) {
        this.value = value;
    }
}
