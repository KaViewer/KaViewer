package com.koy.kaviewer.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicVO {
    private String topicName;
    private Integer partitionSize;
    private Integer replicationFactor;

    public NewTopic buildNewTopic() {
        return new NewTopic(this.topicName, this.partitionSize, this.replicationFactor.shortValue());
    }
}
