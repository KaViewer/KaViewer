package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.core.KafkaSetUpHandler;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ClusterService {
    @Autowired
    ApplicationContext applicationContext;

    public void create(KafkaPropertiesVO kafkaPropertiesVO) {
        final KafkaSetUpHandler handler = applicationContext.getParent().getBean(KafkaSetUpHandler.class);
        final PropertiesResources<KafkaPropertiesVO> propertiesResources = new PropertiesResources<>();
        propertiesResources.setResource(kafkaPropertiesVO);
        propertiesResources.setType(PropertiesResources.ResourcesType.ENTITY);
        handler.setUp(propertiesResources);
    }
}
