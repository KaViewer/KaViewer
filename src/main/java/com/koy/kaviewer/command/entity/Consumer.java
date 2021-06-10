package com.koy.kaviewer.command.entity;

import org.apache.kafka.common.TopicPartition;

import java.util.Set;

public class Consumer {
    // groupId
    private String groupId;
    private String topic;
    private String consumerId;
    private String clientId;
    private Set<TopicPartition> topicPartitions;
    private String host;

    public Consumer() {
    }

    public Consumer(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "groupId='" + groupId + '\'' +
                ", topic='" + topic + '\'' +
                ", memberId='" + consumerId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", topicPartitions=" + topicPartitions +
                ", host=" + host +
                '}';
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Set<TopicPartition> getTopicPartitions() {
        return topicPartitions;
    }

    public void setTopicPartitions(Set<TopicPartition> topicPartitions) {
        this.topicPartitions = topicPartitions;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
