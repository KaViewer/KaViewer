package com.koy.kaviewer.app.service.resolver;

import com.koy.kaviewer.app.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import org.springframework.stereotype.Component;

@Component
public class KafkaPropertiesConvert implements EnvConfigConvert<KaViewerKafkaConfiguration, KafkaProperties> {
    @Override
    public KafkaProperties convert(KaViewerKafkaConfiguration kaViewerKafkaConfiguration) {
        final String cluster = kaViewerKafkaConfiguration.getCluster();
        final String bootstrapService = kaViewerKafkaConfiguration.getBootstrapService();
        final String jaasConfig = kaViewerKafkaConfiguration.getJaasConfig();

        final KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setClusterName(cluster);
        kafkaProperties.setBootstrapServers(bootstrapService);
        kafkaProperties.setJaasConfig(jaasConfig);
        return kafkaProperties.buildProperties();
    }
}
