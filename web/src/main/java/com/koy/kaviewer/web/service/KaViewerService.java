package com.koy.kaviewer.web.service;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.share.RequestContextManagement;
import org.springframework.stereotype.Service;

@Service
public class KaViewerService {

    public KafkaPropertiesVO meta() {
        final String cluster = RequestContextManagement.getCluster();
        final KafkaApplicationCacheEntity kafkaApplication = KafkaApplication.getKafkaApplication(cluster);
        final KafkaProperties kafkaProperties = kafkaApplication.getKafkaProperties();
        final KafkaPropertiesVO kafkaPropertiesVO = new KafkaPropertiesVO();
        kafkaPropertiesVO.setBootstrapServers(kafkaProperties.getBootstrapServers());
        kafkaPropertiesVO.setClusterName(kafkaProperties.getClusterName());
        kafkaPropertiesVO.setJaasConfig(kafkaProperties.getJaasConfig());
        return kafkaPropertiesVO;

    }
}
