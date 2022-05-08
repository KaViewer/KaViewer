package com.koy.kaviewer.kafka.entity;

import com.koy.kaviewer.kafka.core.KafkaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class KafkaApplicationCacheEntity {
    private String clusterName;
    private KafkaProperties kafkaProperties;
    private ConfigurableApplicationContext root;
    private ApplicationContext parentKafkaApplicationContext;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public KafkaProperties getKafkaProperties() {
        return kafkaProperties;
    }

    public void setKafkaProperties(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public ConfigurableApplicationContext getRoot() {
        return root;
    }

    public void setRoot(ConfigurableApplicationContext root) {
        this.root = root;
    }

    public ApplicationContext getParentKafkaApplicationContext() {
        return parentKafkaApplicationContext;
    }

    public void setParentKafkaApplicationContext(ApplicationContext parentKafkaApplicationContext) {
        this.parentKafkaApplicationContext = parentKafkaApplicationContext;
    }

}
