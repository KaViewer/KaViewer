package com.koy.kaviewer.web.domain;

import java.util.List;

public class ClusterVO {
    private String cluster;
    private List<BrokerVO> brokers;

    public ClusterVO(String cluster, List<BrokerVO> brokers) {
        this.cluster = cluster;
        this.brokers = brokers;
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
}

