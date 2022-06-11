package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.ipc.KafkaSetupService;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import org.springframework.stereotype.Service;

@Service
public class ClusterService {

    public void create(KafkaPropertiesVO kafkaPropertiesVO) {
        final String clusterName = kafkaPropertiesVO.getClusterName();
        if (KafkaApplication.contains(clusterName)) {
            return;
        }
        final KafkaSetupService handler = KaViewerRestApplication.getBean(KafkaSetupService.class);
        final PropertiesResources<KafkaPropertiesVO> propertiesResources = new PropertiesResources<>();
        propertiesResources.setResource(kafkaPropertiesVO);
        propertiesResources.setType(PropertiesResources.ResourcesType.ENTITY);
        handler.setUp(propertiesResources);
    }
}
