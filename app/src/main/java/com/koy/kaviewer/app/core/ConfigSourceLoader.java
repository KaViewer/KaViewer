package com.koy.kaviewer.app.core;

import com.koy.kaviewer.kafka.entity.properties.PropertiesResources;
import org.springframework.core.convert.converter.Converter;

public interface ConfigSourceLoader<T, S> {

    T load(S source) throws Exception;

    boolean support(PropertiesResources.ResourcesType type);

    default <V> Converter<T, V> getConvertor() {
        return null;
    }

}
