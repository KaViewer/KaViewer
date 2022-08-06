package com.koy.kaviewer.app.service.endpoint;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KaViewerIntrospectionContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        final List<KafkaApplicationCacheEntity> kafkaContexts = KafkaApplication.listAll();
        builder.withDetail("kafkaContexts", kafkaContexts);
    }

}
