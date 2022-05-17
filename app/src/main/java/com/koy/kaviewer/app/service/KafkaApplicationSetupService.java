package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.KaViewerApplication;
import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.ipc.KafkaSetupService;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.service.KafkaService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaApplicationSetupService implements KafkaSetupService {

    public static final AtomicInteger idx = new AtomicInteger(0);

    public void setUp(ConfigurableApplicationContext parent, String[] args, PropertiesResources resources) throws Exception {

        final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(KafkaApplication.class)
                .parent(parent)
                .web(WebApplicationType.NONE)
                .properties("spring.application.name=KaViewer::KafkaApplication-" + idx.getAndIncrement())
                .profiles("kafka");

        final ConfigurableApplicationContext kafka = applicationBuilder.run(args);
        final KafkaService kafkaService = kafka.getBean(KafkaService.class);
        kafkaService.buildApplication(kafka, resources);
    }


    @Override
    public void setUp(PropertiesResources propertiesResources) {
        try {
            setUp((ConfigurableApplicationContext) KaViewerApplication.getRoot(),
                    KaViewerApplication.getArgs(), propertiesResources);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
