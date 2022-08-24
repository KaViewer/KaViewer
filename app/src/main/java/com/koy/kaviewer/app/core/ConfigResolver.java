package com.koy.kaviewer.app.core;

import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import com.koy.kaviewer.common.entity.properties.PropertiesResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConfigResolver implements ConfigSourceLoader<KafkaProperties, PropertiesResources> {

    private final List<ConfigSourceLoader> configSourceLoaders;

    @Autowired
    public ConfigResolver(List<ConfigSourceLoader> configSourceLoaders) {
        this.configSourceLoaders = configSourceLoaders;
    }


    @Override
    public KafkaProperties load(PropertiesResources source) throws Exception {
        final ConfigSourceLoader sourceLoader = configSourceLoaders.stream().filter(loader -> loader.support(source.getType())).findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Not Support Config File Type !!!"));
        return (KafkaProperties) sourceLoader.getConvertor().convert(sourceLoader.load(source));
    }

    @Override
    public boolean support(PropertiesResources.ResourcesType type) {
        return true;
    }

    @Override
    public <V> Converter<KafkaProperties, V> getConvertor() {
        return null;
    }

}
