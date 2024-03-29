package com.koy.kaviewer.kafka.impl;

import com.koy.kaviewer.common.service.BrokerService;
import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrokerServiceImpl implements BrokerService {

    private final KafkaClientWrapper kafkaClientWrapper;

    @Override
    public DescribeClusterResult describeClusters() {
        return kafkaClientWrapper.describeClusterResult();
    }


}
