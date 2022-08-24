package com.koy.kaviewer.kafka.impl;

import com.koy.kaviewer.common.entity.TopicMetaVO;
import com.koy.kaviewer.common.entity.TopicVO;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import com.koy.kaviewer.common.service.TopicService;
import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.service.KafkaConsumerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final KafkaClientWrapper kafkaClientWrapper;
    private final KafkaConsumerFactory kafkaConsumerFactory;

    @Override
    public Set<String> list(String clusterName) {
        final ListTopicsResult listTopicsResult = kafkaClientWrapper.listTopicsResult();
        try {
            return listTopicsResult.names().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }

    @Override
    public void create(TopicVO topic) {
        try {
            kafkaClientWrapper.createTopic(topic.buildNewTopic());
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }

    }

    @Override
    public void delete(String topicName) {
        try {
            this.kafkaClientWrapper.deleteTopic(topicName);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }

    @Override
    public List<TopicMetaVO> listMeta(String clusterName) {
        return kafkaConsumerFactory.buildTopicsMeta()
                .stream()
                .sorted((t1, t2) -> t1.getTopicName().compareToIgnoreCase(t2.getTopicName())
                ).collect(Collectors.toList());
    }

}
