package com.koy.kaviewer.web.domain;

import java.util.List;

public class ClusterVO {
    private String cluster;
    private List<BrokerVO> brokers;
    private Long createdTime;

    public ClusterVO(String cluster, List<BrokerVO> brokers, Long createdTime) {
        this.cluster = cluster;
        this.brokers = brokers;
        this.createdTime = createdTime;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public List<BrokerVO> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<BrokerVO> brokers) {
        this.brokers = brokers;
    }

    public Long getCreatedTime() {
        return createdTime;
    }
}

