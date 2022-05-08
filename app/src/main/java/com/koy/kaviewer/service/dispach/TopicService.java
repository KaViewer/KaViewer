package com.koy.kaviewer.service.dispach;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TopicService implements com.koy.kaviewer.kafka.core.remote.TopicService {
    @Override
    public Set<String> list(String clusterName) {
        final com.koy.kaviewer.kafka.core.remote.TopicService topicService
                = KafkaApplication.getKafkaApplicationBean(clusterName, com.koy.kaviewer.kafka.core.remote.TopicService.class);
        return topicService.list(clusterName);
    }
}
