package com.koy.kaviewer.app.service.resolver;

import com.koy.kaviewer.kafka.entity.properties.PropertiesResources;
import org.springframework.core.env.Environment;

import java.util.StringJoiner;

public interface EnvResolver<R> {
    String KAVIEWER_PREFIX = "kaviewer.";

    static String kaViewerConfigKey(String... properties) {
        final StringJoiner stringJoiner = new StringJoiner(".", KAVIEWER_PREFIX, "");
        for (String property : properties) {
            stringJoiner.add(property);
        }

        return stringJoiner.toString();

    }


    PropertiesResources<R> load(Environment environment);

    boolean support(PropertiesResources.ResourcesType resourcesType);
}
