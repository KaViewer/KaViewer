package com.koy.kaviewer.kafka.ipc.impl;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import com.koy.kaviewer.kafka.ipc.TopicService;
import com.koy.kaviewer.kafka.service.KafkaConsumerFactory;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    KafkaClientWrapper kafkaClientWrapper;

    @Autowired
    KafkaConsumerFactory kafkaConsumerFactory;

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
    public List<TopicMetaVO> listMeta(String clusterName) {
        return kafkaConsumerFactory.buildTopicsMeta()
                .stream()
                .sorted((t1, t2) -> t1.getTopicName().compareToIgnoreCase(t2.getTopicName())
                ).collect(Collectors.toList());
    }

}
