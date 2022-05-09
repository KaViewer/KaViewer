package com.koy.kaviewer.kafka.client;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.stereotype.Component;

@Component
public class KafkaClientWrapper {

    private AdminClient delegate;

    public KafkaClientWrapper() {
    }

    public ListTopicsResult listTopicsResult() {
        return delegate.listTopics();
    }

    public DescribeClusterResult describeClusterResult() {
        return delegate.describeCluster();
    }

    public void setDelegate(AdminClient delegate) {
        this.delegate = delegate;
    }
}
