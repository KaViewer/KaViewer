package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import com.koy.kaviewer.kafka.ipc.KafkaSetupService;
import com.koy.kaviewer.kafka.core.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

@Service
public class ClusterService {

    public void create(KafkaPropertiesVO kafkaPropertiesVO) {
        final String clusterName = kafkaPropertiesVO.getClusterName();
        if (KafkaApplication.contains(clusterName)) {
            throw new KaViewerBizException(ErrorMsg.CLUSTER_EXIST);
        }
        final KafkaSetupService handler = KaViewerRestApplication.getBean(KafkaSetupService.class);
        final PropertiesResources<KafkaPropertiesVO> propertiesResources = new PropertiesResources<>();
        propertiesResources.setResource(kafkaPropertiesVO);
        propertiesResources.setType(PropertiesResources.ResourcesType.ENTITY);
        handler.setUp(propertiesResources);
    }

    public void create(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return;
        }

        final PropertiesResources.ResourcesType type = PropertiesResources.ResourcesType.from(multipartFile.getOriginalFilename());
        if (Objects.isNull(type)) {
            return;
        }

        try {
            final KafkaSetupService handler = KaViewerRestApplication.getBean(KafkaSetupService.class);
            final PropertiesResources<InputStream> propertiesResources = new PropertiesResources<>();
            propertiesResources.setResource(multipartFile.getInputStream());
            propertiesResources.setType(type);
            handler.setUp(propertiesResources);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String clusterName) {
        KafkaApplication.remove(clusterName);
    }

}
