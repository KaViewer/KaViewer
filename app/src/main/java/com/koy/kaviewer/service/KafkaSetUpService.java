package com.koy.kaviewer.service;

import com.koy.kaviewer.KaViewerApplication;
import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.core.ConfigResolver;
import com.koy.kaviewer.kafka.core.KafkaProperties;
import com.koy.kaviewer.kafka.core.KafkaSetUpHandler;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.kafka.service.KafkaService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaSetUpService implements KafkaSetUpHandler {

    public static final AtomicInteger idx = new AtomicInteger(0);

    public void setUp(ConfigurableApplicationContext parent, String[] args, PropertiesResources resources) throws Exception {

        final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(KafkaApplication.class)
                .parent(parent)
                .web(WebApplicationType.NONE)
                .properties("spring.application.name=KaViewer::KafkaApplication-" + idx.getAndIncrement())
                .profiles("kafka");

        final ConfigurableApplicationContext kafka = applicationBuilder.run(args);
        final KafkaService kafkaService = kafka.getBean(KafkaService.class);
        kafkaService.buildApplication(resources);
//        final ConfigurableApplicationContext client = applicationBuilder.child(ClientApplication.class)
//                .web(WebApplicationType.NONE)
//                .parent(kafka)
//                .properties("spring.application.name=KaViewer-Kafka-Client")
//                .run(args);
    }


    @Override
    public void setUp(PropertiesResources propertiesResources) {
        try {
            setUp((ConfigurableApplicationContext) KaViewerApplication.getRoot(), KaViewerApplication.getArgs(), propertiesResources);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
