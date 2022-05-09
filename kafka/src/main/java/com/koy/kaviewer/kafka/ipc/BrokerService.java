package com.koy.kaviewer.kafka.ipc;

import org.apache.kafka.clients.admin.DescribeClusterResult;

public interface BrokerService {
    DescribeClusterResult describeClusters(String clusterName);
}
