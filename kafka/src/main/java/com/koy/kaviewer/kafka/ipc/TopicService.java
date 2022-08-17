package com.koy.kaviewer.kafka.ipc;

import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import com.koy.kaviewer.kafka.entity.TopicVO;

import java.util.List;
import java.util.Set;

public interface TopicService {
    Set<String> list(String clusterName);

    void create(TopicVO topicName);

    void delete(String topicName);


    List<TopicMetaVO> listMeta(String clusterName);
}
