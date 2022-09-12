package com.koy.kaviewer.app.persist;

import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import org.springframework.util.ClassUtils;

import java.util.List;

public interface Persistent {

    default ClassLoader getClassloader() {
        return ClassUtils.getDefaultClassLoader();
    }

    List<KafkaProperties> load();

    void persist(List<KafkaProperties> kafkaProperties);
}
