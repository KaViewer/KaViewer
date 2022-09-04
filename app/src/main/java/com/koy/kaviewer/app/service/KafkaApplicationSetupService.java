package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.KaViewerApplication;

import com.koy.kaviewer.app.core.ConfigResolver;
import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import com.koy.kaviewer.common.entity.properties.PropertiesResources;
import com.koy.kaviewer.common.exception.ErrorMsg;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import com.koy.kaviewer.common.service.KafkaSetupService;
import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaApplicationSetupService implements KafkaSetupService {
    private final ConfigResolver configResolver;

    @Autowired
    public KafkaApplicationSetupService(ConfigResolver configResolver) {
        this.configResolver = configResolver;
    }

    // IDEA, disable JMX agent
    public void setUp(KafkaProperties kafkaProperties) {
        try {
            log.info("Start to setup Kafka based on properties, cluster:[{}], boostrapServes:[{}]",
                    kafkaProperties.getClusterName(), kafkaProperties.getBootstrapServers());
            setUp((ConfigurableApplicationContext) KaViewerApplication.getRoot(),
                    KaViewerApplication.getArgs(), kafkaProperties);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("setup Kafka error: ", e);
        }
    }

    public void setUp(ConfigurableApplicationContext parent, String[] args, KafkaProperties kafkaProperties) throws Exception {
        final String clusterName = kafkaProperties.getClusterName();
        if (KafkaApplicationHolder.contains(clusterName)) {
            log.warn("Found duplicated cluster for creating, cluster:[{}]", clusterName);
            throw KaViewerBizException.of(ErrorMsg.CLUSTER_EXIST);
        }

        final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(KafkaApplication.class)
                .parent(parent)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .properties("spring.application.name=KaViewer::KafkaApplication-" + clusterName)
                .profiles("kafka");

        final ConfigurableApplicationContext kafka = applicationBuilder.run(args);
        final String kafkaCtxId = KafkaApplication.class.getSimpleName() + "-" + clusterName;
        log.info("Set new KafkaApplicationContent with id:[{}]", kafkaCtxId);
        kafka.setId(kafkaCtxId);
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
