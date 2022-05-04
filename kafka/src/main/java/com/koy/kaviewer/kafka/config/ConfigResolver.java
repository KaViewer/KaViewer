package com.koy.kaviewer.kafka.config;

import ch.qos.logback.classic.pattern.LineSeparatorConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Order(1)
@Service
public class ConfigResolver implements ConfigSourceLoader<KafkaProperties, String> {

    @Autowired
    private List<ConfigSourceLoader<Map<String, Object>, String>> configSourceLoaders;

    @Override
    public KafkaProperties load(String source) throws FileNotFoundException {
        final ConfigSourceLoader<Map<String, Object>, String> sourceLoader = configSourceLoaders.stream().filter(loader -> !Objects.equals(this, loader)).filter(loader -> support(source)).findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Not Support Config File Type !!!"));
        Map<String, Object> configs = sourceLoader.load(source);
        final KafkaProperties kafkaProperties = new KafkaProperties();
        return kafkaProperties.buildProperties(configs);

    }

    @Override
    public boolean support(String fileName) {
        return true;
    }

}
