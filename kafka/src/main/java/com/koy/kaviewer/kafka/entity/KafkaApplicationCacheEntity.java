package com.koy.kaviewer.kafka.entity;

import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class KafkaApplicationCacheEntity implements Cloneable {
    private String clusterName;
    private KafkaProperties kafkaProperties;
    private ConfigurableApplicationContext root;
    private ApplicationContext kafkaApplicationContext;
    private final Long createTimestamp = System.currentTimeMillis();

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

    public ApplicationContext getKafkaApplicationContext() {
        return kafkaApplicationContext;
    }

    public void setKafkaApplicationContext(ApplicationContext kafkaApplicationContext) {
        this.kafkaApplicationContext = kafkaApplicationContext;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    @Override
    public KafkaApplicationCacheEntity clone() {
        final KafkaApplicationCacheEntity clone = new KafkaApplicationCacheEntity();
        clone.setRoot(this.root);
        clone.setKafkaApplicationContext(this.kafkaApplicationContext);
        clone.setClusterName(this.clusterName);

        final KafkaProperties kafkaProperties = this.getKafkaProperties();
        final KafkaProperties kafkaPropertiesClone = new KafkaProperties();
        kafkaPropertiesClone.putAll(kafkaProperties);
        // mask security
        kafkaPropertiesClone.put(SaslConfigs.SASL_JAAS_CONFIG, Optional.ofNullable(kafkaProperties.getJaasConfig())
                .orElseGet(() -> StringUtils.EMPTY)
                .replaceAll("(.*username=).*(password=).*", "$1****** $2*****"));
        clone.setKafkaProperties(kafkaPropertiesClone);
        return clone;
    }
}
