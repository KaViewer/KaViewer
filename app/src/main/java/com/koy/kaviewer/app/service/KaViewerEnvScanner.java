package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.service.resolver.EnvResolver;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;


@Service
public class KaViewerEnvScanner {

    @Autowired
    List<EnvResolver> envResolvers;
    @Autowired
    KafkaApplicationSetupService kafkaApplicationSetupService;

    @PostConstruct
    public void scanEnv(Environment environment) {
        final String type = environment.getProperty(EnvResolver.kaViewerConfigKey("type"));
        if (StringUtils.isEmpty(type)) {
            return;
        }
        final PropertiesResources.ResourcesType resourceType = PropertiesResources.ResourcesType.from(type);

        if (Objects.isNull(resourceType)) {
            return;
        }

        envResolvers.stream().filter(resolver -> resolver.support(resourceType)).findFirst().map(it -> it.load(environment))
                .ifPresent(resources -> kafkaApplicationSetupService.setUp(resources));

    }

}
