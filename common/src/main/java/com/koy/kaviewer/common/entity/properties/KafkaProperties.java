package com.koy.kaviewer.common.entity.properties;

import com.koy.kaviewer.common.entity.BrokerSecurityType;
import com.koy.kaviewer.common.exception.ErrorMsg;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.Properties;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class KafkaProperties extends Properties implements Cloneable {
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

    @Override
    public KafkaProperties clone() {
        final KafkaProperties clone = new KafkaProperties();
        clone.setClusterName(clusterName);
        clone.setBootstrapServers(bootstrapServers);
        clone.setJaasConfig(jaasConfig);
        return clone;
    }

    static class Security {
        private final String type = BrokerSecurityType.SASL_SSL.name();

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

    public void setBootstrapServers(String bootstrapServers) {
        assert StringUtils.isNotEmpty(bootstrapServers);
        this.bootstrapServers = bootstrapServers;
    }

    public String getClusterKey() {
        return clusterName + "-" + kafkaClusterVersion;
    }

    public ConsumerProperties getConsumerProperties() {
        return this.consumer.buildConsumerProperties();
    }

    public ProducerProperties getProducerProperties() {
        return this.producer.buildProducerProperties();
    }
}
