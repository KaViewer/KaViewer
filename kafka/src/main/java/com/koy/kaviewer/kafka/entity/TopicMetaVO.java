package com.koy.kaviewer.kafka.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.List;

public class TopicMetaVO {
    private String topicName;
    private int partitionSize;
    private List<PartitionInfo> partitions;
    private List<TopicPartition> topicPartitions;

    public TopicMetaVO(String topicName, List<PartitionInfo> partitions, List<TopicPartition> topicPartitions) {
        this.topicName = topicName;
        this.partitions = partitions;
        this.partitionSize = partitions.size();
        this.topicPartitions = topicPartitions;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getPartitionSize() {
        return partitionSize;
    }

    @JsonIgnore
    public List<TopicPartition> getTopicPartitions() {
        return topicPartitions;
    }

    @JsonIgnore
    public List<PartitionInfo> getPartitions() {
        return partitions;
    }
}
