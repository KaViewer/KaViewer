package com.koy.kaviewer.app.service.endpoint;

import com.koy.kaviewer.common.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.kafka.application.KafkaApplication;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KaViewerIntrospectionContributor implements InfoContributor {

    private static final String KAFKA_CONTEXTS = "kafkaContexts";

    @Override
    public void contribute(Info.Builder builder) {
        final List<KafkaApplicationCacheEntity> kafkaContexts = KafkaApplication.listAll();
        builder.withDetail(KAFKA_CONTEXTS, kafkaContexts);
    }

}
