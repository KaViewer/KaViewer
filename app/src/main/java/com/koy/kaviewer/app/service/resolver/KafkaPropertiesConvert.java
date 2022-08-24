package com.koy.kaviewer.app.service.resolver;

import com.koy.kaviewer.app.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import org.springframework.stereotype.Component;

@Component
public class KafkaPropertiesConvert implements EnvConfigConvert<KaViewerKafkaConfiguration, KafkaProperties> {
    @Override
    public KafkaProperties convert(KaViewerKafkaConfiguration kaViewerKafkaConfiguration) {
        final String cluster = kaViewerKafkaConfiguration.getCluster();
        final String bootstrapServers = kaViewerKafkaConfiguration.getBootstrapServers();
        final String jaasConfig = kaViewerKafkaConfiguration.getJaasConfig();

        final KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setClusterName(cluster);
        kafkaProperties.setBootstrapServers(bootstrapServers);
        kafkaProperties.setJaasConfig(jaasConfig);
        return kafkaProperties.buildProperties();
    }
}
