package com.koy.kaviewer.web.service;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.common.entity.KafkaPropertiesVO;
import com.koy.kaviewer.common.entity.properties.PropertiesResources;
import com.koy.kaviewer.common.exception.ErrorMsg;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import com.koy.kaviewer.common.ipc.KafkaSetupService;
import com.koy.kaviewer.web.KaViewerWebApplication;
import com.koy.kaviewer.web.domain.ClusterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClusterService {

    private final BrokerBizService brokerBizService;

    public List<KafkaApplicationCacheEntity> list() {
        return KafkaApplicationHolder.listAll();
    }

    public void create(KafkaPropertiesVO kafkaPropertiesVO) {
        final String clusterName = kafkaPropertiesVO.getClusterName();
        if (KafkaApplicationHolder.contains(clusterName)) {
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
        KafkaApplicationHolder.remove(clusterName);
    }

    public List<ClusterVO> meta() {
        final List<KafkaApplicationCacheEntity> clusters = list();
        return clusters.stream().map(cluster ->
                        new ClusterVO(cluster.getClusterName(), brokerBizService.brokers(cluster.getClusterName()), KafkaApplicationHolder.getKafkaApplication(cluster.getClusterName()).getCreateTimestamp()))
                .sorted(Comparator.comparingLong(ClusterVO::getCreatedTime)).collect(Collectors.toList());
    }
}
