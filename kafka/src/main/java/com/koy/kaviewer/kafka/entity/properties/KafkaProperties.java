package com.koy.kaviewer.kafka.entity.properties;

import com.koy.kaviewer.kafka.entity.BrokerSecurityType;
import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.Properties;

public class KafkaProperties extends Properties {
    private Integer consumerWorkerSize = 3;
    private String encoding = "UTF8";
    private String clusterName;
    private String kafkaClusterVersion;
    private String ZookeeperHost;
    private String zookeeperPort = "2181";
    private String bootstrapServers;
    private String jaasConfig;
    private String SASLMechanism = "PLAIN";
    private Security security = new Security();
    private String clientId;
    private final ConsumerProperties consumer = new ConsumerProperties(this);
    private final ProducerProperties producer = new ProducerProperties(this);

    static class Security {
        private String type = BrokerSecurityType.SASL_SSL.name();

        public void config(KafkaProperties kafkaProperties) {
            kafkaProperties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, type);
        }
    }

    public KafkaProperties buildProperties() {
        if (!isValid()) {
            throw KaViewerBizException.of(ErrorMsg.NO_CLUSTER_META);
        }
        this.clientId = "KaViewer::" + clusterName;
        setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        setProperty(AdminClientConfig.CLIENT_ID_CONFIG, this.clientId);
        final String jaasConfig = getJaasConfig();
        if (StringUtils.isNotEmpty(jaasConfig)) {
            setProperty(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
            setProperty(SaslConfigs.SASL_MECHANISM, SASLMechanism);
            security.config(this);
        }
        return this;
    }

    private boolean isValid() {
        return StringUtils.isNotEmpty(this.bootstrapServers) && StringUtils.isNotEmpty(clusterName);
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        if (StringUtils.isEmpty(clusterName)) {
            throw KaViewerBizException.of(ErrorMsg.NO_CLUSTER_META);
        }
        this.clusterName = clusterName;
    }

    public Integer getConsumerWorkerSize() {
        return consumerWorkerSize;
    }

    public void setConsumerWorkerSize(Integer consumerWorkerSize) {
        this.consumerWorkerSize = consumerWorkerSize;
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

    public String getJaasConfig() {
        return jaasConfig;
    }

    public void setJaasConfig(String jaasConfig) {
        this.jaasConfig = jaasConfig;
    }

    public String getKey() {
        return clusterName + "-" + kafkaClusterVersion;
    }

    public ConsumerProperties getConsumer() {
        return this.consumer.buildConsumerProperties();
    }

    public ProducerProperties getProducer() {
        return this.producer.buildProducerProperties();
    }

    public String getEncoding() {
        return encoding;
    }

}
