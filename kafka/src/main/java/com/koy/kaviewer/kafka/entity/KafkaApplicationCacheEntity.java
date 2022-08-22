package com.koy.kaviewer.kafka.entity;

import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

@Data
public class KafkaApplicationCacheEntity implements Cloneable {
    private String clusterName;
    private KafkaProperties kafkaProperties;
    private ConfigurableApplicationContext root;
    private ApplicationContext kafkaApplicationContext;
    private final Long createTimestamp = System.currentTimeMillis();

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
