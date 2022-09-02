package com.koy.kaviewer.app.persist;

import com.koy.kaviewer.common.entity.properties.KafkaProperties;

import java.util.List;

public interface Persistent {

    default ClassLoader getClassloader() {
        return PersistKafkaPropertiesHandler.class.getClassLoader();
    }

    List<KafkaProperties> load();

    void persist(List<KafkaProperties> kafkaProperties);
}
