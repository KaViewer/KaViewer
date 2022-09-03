package com.koy.kaviewer.web.service;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.common.entity.KafkaPropertiesVO;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import com.koy.kaviewer.web.core.RequestContextManagement;
import org.springframework.stereotype.Service;

@Service
public class MetaService {

    public KafkaPropertiesVO meta() {
        final String cluster = RequestContextManagement.getCluster();
        final KafkaApplicationCacheEntity kafkaApplication = KafkaApplicationHolder.getKafkaApplication(cluster).clone();
        final KafkaProperties kafkaProperties = kafkaApplication.getKafkaProperties();
        final KafkaPropertiesVO kafkaPropertiesVO = new KafkaPropertiesVO();
        kafkaPropertiesVO.setBootstrapServers(kafkaProperties.getBootstrapServers());
        kafkaPropertiesVO.setClusterName(kafkaProperties.getClusterName());
        kafkaPropertiesVO.setJaasConfig(kafkaProperties.getJaasConfig());
        return kafkaPropertiesVO;

    }
}
