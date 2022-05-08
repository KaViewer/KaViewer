package com.koy.kaviewer.kafka.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.core.convert.converter.Converter;

import java.util.Properties;

public class KafkaProperties extends Properties {
    private String key;
    private String clusterName;
    private String kafkaClusterVersion;
    private String ZookeeperHost;
    private String zookeeperPort = "2181";
    private String bootstrapServers;
    private Security security;


    static class Security {
        private String type;
    }

    public KafkaProperties buildProperties() {
        setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        setProperty(AdminClientConfig.CLIENT_ID_CONFIG, "KaViewer::" + clusterName);
        return this;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getKafkaClusterVersion() {
        return kafkaClusterVersion;
    }

    public void setKafkaClusterVersion(String kafkaClusterVersion) {
        this.kafkaClusterVersion = kafkaClusterVersion;
    }

    public String getZookeeperHost() {
        return ZookeeperHost;
    }

    public void setZookeeperHost(String zookeeperHost) {
        ZookeeperHost = zookeeperHost;
    }

    public String getZookeeperPort() {
        return zookeeperPort;
    }

    public void setZookeeperPort(String zookeeperPort) {
        this.zookeeperPort = zookeeperPort;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        assert StringUtils.isNotEmpty(bootstrapServers);
        this.bootstrapServers = bootstrapServers;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getKey() {
        return clusterName + "-" + kafkaClusterVersion;
    }

    interface KafkaPropertiesConverter<S> extends Converter<S, KafkaProperties> {


    }
}
