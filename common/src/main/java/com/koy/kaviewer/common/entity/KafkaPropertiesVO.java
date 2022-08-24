package com.koy.kaviewer.common.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KafkaPropertiesVO {
    private String clusterName;
    private String kafkaClusterVersion;
    private String ZookeeperHost;
    private String zookeeperPort = "2181";
    private String bootstrapServers;
    private String jaasConfig;
}

