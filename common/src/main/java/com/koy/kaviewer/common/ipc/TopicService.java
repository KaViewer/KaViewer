package com.koy.kaviewer.common.ipc;

import com.koy.kaviewer.common.entity.TopicMetaVO;
import com.koy.kaviewer.common.entity.TopicVO;

import java.util.List;
import java.util.Set;

public interface TopicService {
    Set<String> list(String clusterName);

    void create(TopicVO topicName);

    void delete(String topicName);


    List<TopicMetaVO> listMeta(String clusterName);
}
