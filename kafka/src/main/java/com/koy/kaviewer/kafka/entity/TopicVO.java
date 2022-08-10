package com.koy.kaviewer.kafka.entity;

import org.apache.kafka.clients.admin.NewTopic;

public class TopicVO {
    private String topicName;
    private Integer partitionSize;
    private Integer replicationFactor;

    public TopicVO() {
    }

    public TopicVO(String topicName, Integer partitionSize, Integer replicationFactor) {
        this.topicName = topicName;
        this.partitionSize = partitionSize;
        this.replicationFactor = replicationFactor;
    }

    public NewTopic buildNewTopic() {
        return new NewTopic(this.topicName, this.partitionSize,this.replicationFactor.shortValue());
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getPartitionSize() {
        return partitionSize;
    }

    public void setPartitionSize(int partitionSize) {
        this.partitionSize = partitionSize;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}
