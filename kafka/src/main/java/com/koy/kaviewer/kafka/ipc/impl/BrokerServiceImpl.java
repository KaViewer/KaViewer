package com.koy.kaviewer.kafka.ipc.impl;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.ipc.BrokerService;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerServiceImpl implements BrokerService {

    private final KafkaClientWrapper kafkaClientWrapper;

    @Autowired
    public BrokerServiceImpl(KafkaClientWrapper kafkaClientWrapper) {
        this.kafkaClientWrapper = kafkaClientWrapper;
    }

    @Override
    public DescribeClusterResult describeClusters() {
        return kafkaClientWrapper.describeClusterResult();
    }


}
