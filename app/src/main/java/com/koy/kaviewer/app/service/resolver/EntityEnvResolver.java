package com.koy.kaviewer.app.service.resolver;

import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EntityEnvResolver implements EnvResolver<KafkaPropertiesVO> {
    @Override
    public PropertiesResources<KafkaPropertiesVO> load(Environment environment) {
        final KafkaPropertiesVO kafkaPropertiesVO = new KafkaPropertiesVO();
        final String cluster = environment.getProperty(EnvResolver.kaViewerConfigKey("cluster"), String.class);
        final String bootstrap = environment.getProperty(EnvResolver.kaViewerConfigKey("bootstrap"), String.class);
        kafkaPropertiesVO.setClusterName(cluster);
        kafkaPropertiesVO.setBootstrapServers(bootstrap);

        final PropertiesResources<KafkaPropertiesVO> resources = new PropertiesResources<>();
        resources.setType(PropertiesResources.ResourcesType.ENTITY);
        resources.setResource(kafkaPropertiesVO);
        return resources;
    }

    @Override
    public boolean support(PropertiesResources.ResourcesType resourcesType) {
        return PropertiesResources.ResourcesType.ENTITY == resourcesType;
    }
}
