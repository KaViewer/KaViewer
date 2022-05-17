package com.koy.kaviewer.app.ipc.topic;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TopicService implements com.koy.kaviewer.kafka.ipc.TopicService {
    @Override
    public Set<String> list(String clusterName) {
        final com.koy.kaviewer.kafka.ipc.TopicService topicService
                = KafkaApplication.getKafkaApplicationBean(clusterName, com.koy.kaviewer.kafka.ipc.TopicService.class);
        return topicService.list(clusterName);
    }

    @Override
    public List<TopicMetaVO> listMeta(String clusterName) {
        final com.koy.kaviewer.kafka.ipc.TopicService topicService
                = KafkaApplication.getKafkaApplicationBean(clusterName, com.koy.kaviewer.kafka.ipc.TopicService.class);
        return topicService.listMeta(clusterName);
    }
}
