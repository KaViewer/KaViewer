package com.koy.kaviewer.common.ipc;

import org.apache.kafka.clients.admin.DescribeClusterResult;

public interface BrokerService {
    DescribeClusterResult describeClusters();
}
