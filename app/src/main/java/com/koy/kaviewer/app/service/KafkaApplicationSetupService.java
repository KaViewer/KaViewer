package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.KaViewerApplication;
import com.koy.kaviewer.app.core.ConfigResolver;
import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import com.koy.kaviewer.kafka.ipc.KafkaSetupService;
import com.koy.kaviewer.kafka.entity.properties.PropertiesResources;
import com.koy.kaviewer.kafka.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class KafkaApplicationSetupService implements KafkaSetupService {
    private final ConfigResolver configResolver;

    @Autowired
    public KafkaApplicationSetupService(ConfigResolver configResolver) {
        this.configResolver = configResolver;
    }

    // IDEA, disable JMX agent
    public void setUp(KafkaProperties kafkaProperties) throws Exception {
        setUp((ConfigurableApplicationContext) KaViewerApplication.getRoot(),
                KaViewerApplication.getArgs(), kafkaProperties);
    }

    public void setUp(ConfigurableApplicationContext parent, String[] args, KafkaProperties kafkaProperties) throws Exception {
        final String clusterName = kafkaProperties.getClusterName();
        if (KafkaApplication.contains(clusterName)) {
            throw KaViewerBizException.of(ErrorMsg.CLUSTER_EXIST);
        }

        final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(KafkaApplication.class)
                .parent(parent)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .properties("spring.application.name=KaViewer::KafkaApplication-" + clusterName)
                .profiles("kafka");

        final ConfigurableApplicationContext kafka = applicationBuilder.run(args);
        kafka.setId(KafkaApplication.class.getSimpleName() + "-" + clusterName);
        final KafkaService kafkaService = kafka.getBean(KafkaService.class);
        kafkaService.buildApplication(kafkaProperties, kafka);
    }


    @Override
    public void setUp(PropertiesResources propertiesResources) {
        try {
            final KafkaProperties kafkaProperties = configResolver.load(propertiesResources);
            setUp(kafkaProperties);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }
}
