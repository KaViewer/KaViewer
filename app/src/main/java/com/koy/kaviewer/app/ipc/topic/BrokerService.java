package com.koy.kaviewer.app.ipc.topic;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.stereotype.Service;


@Service
public class BrokerService implements com.koy.kaviewer.kafka.ipc.BrokerService {
    @Override
    public DescribeClusterResult describeClusters(String clusterName) {
        final com.koy.kaviewer.kafka.ipc.BrokerService brokerService
                = KafkaApplication.getKafkaApplicationBean(clusterName, com.koy.kaviewer.kafka.ipc.BrokerService.class);
        return brokerService.describeClusters(clusterName);
    }
}
