package com.koy.kaviewer.kafka.impl;

import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.ipc.BrokerService;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerServiceImpl implements BrokerService {
    @Autowired
    KafkaClientWrapper kafkaClientWrapper;

    @Override
    public DescribeClusterResult describeClusters(String clusterName) {
        return kafkaClientWrapper.describeClusterResult();
    }


}
