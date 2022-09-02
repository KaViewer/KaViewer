package com.koy.kaviewer.app.persist;

import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

// Test
@Slf4j
public class EmptyPersistentHandler implements Persistent {
    @Override
    public List<KafkaProperties> load() {
        log.info("Load Persistent Kafka Properties, size: [{}]", 0);
        return List.of();
    }

    @Override
    public void persist(List<KafkaProperties> kafkaProperties) {
        log.info("Persist Kafka Properties, size: [{}]", kafkaProperties.size());
    }
}
