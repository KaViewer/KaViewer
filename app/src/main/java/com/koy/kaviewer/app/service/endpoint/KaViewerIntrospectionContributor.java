package com.koy.kaviewer.app.service.endpoint;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KaViewerIntrospectionContributor implements InfoContributor {

    @Override
    // TODO add more info
    public void contribute(Info.Builder builder) {
        final List<String> kafkaContexts = KafkaApplication.listAll();
        builder.withDetail("kafkaContexts", kafkaContexts);
    }

}
