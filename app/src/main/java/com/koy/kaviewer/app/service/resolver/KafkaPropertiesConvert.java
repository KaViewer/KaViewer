package com.koy.kaviewer.app.service.resolver;

import com.koy.kaviewer.common.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class KafkaPropertiesConvert implements EnvConfigConvert<KaViewerKafkaConfiguration, KafkaProperties> {
    @Override
    public KafkaProperties convert(KaViewerKafkaConfiguration kaViewerKafkaConfiguration) {
        final String cluster = kaViewerKafkaConfiguration.getCluster();
        final String bootstrapServers = kaViewerKafkaConfiguration.getBootstrapServers();
        final String jaasConfig = kaViewerKafkaConfiguration.getJaasConfig();
        final String saslMechanism = kaViewerKafkaConfiguration.getSaslMechanism();
        final String securityProtocol = kaViewerKafkaConfiguration.getSecurityProtocol();

        final KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setClusterName(cluster);
        kafkaProperties.setBootstrapServers(bootstrapServers);
        if (StringUtils.isNotEmpty(jaasConfig)) {
            kafkaProperties.setJaasConfig(jaasConfig);
        }
        if (StringUtils.isNotEmpty(saslMechanism)) {
            kafkaProperties.setSaslMechanism(saslMechanism);
        }

        if (StringUtils.isNotEmpty(securityProtocol)) {
            kafkaProperties.setSecurityProtocol(securityProtocol);
        }
        return kafkaProperties.buildProperties();
    }
}
