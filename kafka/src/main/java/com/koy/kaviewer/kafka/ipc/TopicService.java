package com.koy.kaviewer.kafka.ipc;

import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.Set;

public interface TopicService {
    Set<String> list(String clusterName);

    void create(NewTopic topicName);


    List<TopicMetaVO> listMeta(String clusterName);
}
