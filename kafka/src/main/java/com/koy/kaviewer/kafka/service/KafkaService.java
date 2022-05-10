package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.core.ConfigResolver;
import com.koy.kaviewer.kafka.core.KafkaProperties;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    ConfigResolver configResolver;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    KafkaClientWrapper kafkaAdminClient;

    private KafkaProperties kafkaProperties;

    public void buildApplication(PropertiesResources resources) throws Exception {
        this.kafkaProperties = configResolver.load(resources);

        initKafkaAdminClient(kafkaProperties);

        final KafkaApplicationCacheEntity kafkaApplicationCacheEntity = new KafkaApplicationCacheEntity();
        kafkaApplicationCacheEntity.setClusterName(kafkaProperties.getClusterName());
        kafkaApplicationCacheEntity.setRoot((ConfigurableApplicationContext) applicationContext.getParent());
        kafkaApplicationCacheEntity.setParentKafkaApplicationContext(applicationContext);
        KafkaApplication.putIfAbsent(kafkaApplicationCacheEntity);
    }


    private void initKafkaAdminClient(KafkaProperties kafkaProperties) {
        final AdminClient adminClient = AdminClient.create(kafkaProperties);
        kafkaAdminClient.setDelegate(adminClient);
    }
}
