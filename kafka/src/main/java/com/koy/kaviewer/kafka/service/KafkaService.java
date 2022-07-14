package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.client.KafkaClientWrapper;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final ApplicationContext applicationContext;
    private final KafkaConsumerFactory kafkaConsumerFactory;
    private final KafkaClientWrapper kafkaAdminClient;

    public KafkaService(ApplicationContext applicationContext, KafkaConsumerFactory kafkaConsumerFactory, KafkaClientWrapper kafkaAdminClient) {
        this.applicationContext = applicationContext;
        this.kafkaConsumerFactory = kafkaConsumerFactory;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    public void buildApplication(KafkaProperties kafkaProperties, ConfigurableApplicationContext self) throws Exception {
        if (KafkaApplication.contains(kafkaProperties.getClusterName())) {
            self.close();
            throw KaViewerBizException.of(ErrorMsg.CLUSTER_EXIST);
        }
        try {
            initKafkaAdminClient(kafkaProperties);
            initKafkaConsumerFactory(kafkaProperties);
        } catch (Exception e) {
            e.printStackTrace();

            kafkaAdminClient.close();
            self.close();
            throw KaViewerBizException.of(ErrorMsg.INIT_ERROR, e);
        }

        final KafkaApplicationCacheEntity kafkaApplicationCacheEntity = new KafkaApplicationCacheEntity();
        kafkaApplicationCacheEntity.setClusterName(kafkaProperties.getClusterName());
        kafkaApplicationCacheEntity.setRoot((ConfigurableApplicationContext) applicationContext.getParent());
        kafkaApplicationCacheEntity.setKafkaApplicationContext(applicationContext);
        KafkaApplication.putIfAbsent(kafkaApplicationCacheEntity);
    }


    private void initKafkaAdminClient(KafkaProperties kafkaProperties) {
        final AdminClient adminClient = AdminClient.create(kafkaProperties);

        kafkaAdminClient.setDelegate(adminClient);
        kafkaAdminClient.setKafkaProperties(kafkaProperties);
    }

    private void initKafkaConsumerFactory(KafkaProperties kafkaProperties) {
        kafkaConsumerFactory.createConsumer(kafkaProperties);
    }
}
