package com.koy.kaviewer.web.service;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import com.koy.kaviewer.kafka.ipc.KafkaSetupService;
import com.koy.kaviewer.kafka.entity.properties.PropertiesResources;
import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.web.KaViewerWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
public class ClusterService {

    public List<String> list() {
        return KafkaApplication.listAll();

    }

    public void create(KafkaPropertiesVO kafkaPropertiesVO) {
        final String clusterName = kafkaPropertiesVO.getClusterName();
        if (KafkaApplication.contains(clusterName)) {
            throw KaViewerBizException.of(ErrorMsg.CLUSTER_EXIST);
        }
        final KafkaSetupService handler = KaViewerWebApplication.getBean(KafkaSetupService.class);
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
            final KafkaSetupService handler = KaViewerWebApplication.getBean(KafkaSetupService.class);
            final PropertiesResources<InputStream> propertiesResources = new PropertiesResources<>();
            propertiesResources.setResource(multipartFile.getInputStream());
            propertiesResources.setType(type);
            handler.setUp(propertiesResources);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }

    public void delete(String clusterName) {
        KafkaApplication.remove(clusterName);
    }

}
