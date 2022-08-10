package com.koy.kaviewer.kafka.client;

import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaClientWrapper {

    private AdminClient delegate;
    private KafkaProperties kafkaProperties;

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

    public void close() {
        this.delegate.close();
    }

    public KafkaProperties getKafkaProperties() {
        return kafkaProperties;
    }

    public void setKafkaProperties(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public void createTopic(NewTopic topic) throws ExecutionException, InterruptedException {
        this.delegate.createTopics(List.of(topic)).all().get();
    }
}
