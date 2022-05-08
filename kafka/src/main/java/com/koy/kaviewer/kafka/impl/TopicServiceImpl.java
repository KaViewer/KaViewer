package com.koy.kaviewer.kafka.impl;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.core.remote.TopicService;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    KafkaClientWrapper kafkaClientWrapper;

    @Override
    public Set<String> list(String clusterName) {
        final ListTopicsResult listTopicsResult = kafkaClientWrapper.listTopicsResult();
        try {
            return listTopicsResult.names().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
