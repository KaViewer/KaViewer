package com.koy.kaviewer.common.service;

import org.apache.kafka.clients.admin.DescribeClusterResult;

public interface BrokerService {
    DescribeClusterResult describeClusters();
}
