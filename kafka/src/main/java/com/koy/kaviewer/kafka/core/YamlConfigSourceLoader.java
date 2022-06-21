package com.koy.kaviewer.kafka.core;

import com.koy.kaviewer.kafka.entity.properties.KafkaProperties;
import com.koy.kaviewer.kafka.entity.properties.KafkaPropertiesConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class YamlConfigSourceLoader implements ConfigSourceLoader<Map<String, Object>, PropertiesResources<InputStream>> {

    @Override
    public Map<String, Object> load(PropertiesResources<InputStream> source) throws Exception {
        final Yaml yaml = new Yaml();
        return yaml.load(source.getResource());
    }

    @Override
    public boolean support(PropertiesResources.ResourcesType type) {
        return PropertiesResources.ResourcesType.YAML.equals(type);
    }

    @Override
    public <V> Converter<Map<String, Object>, V> getConvertor() {
        return (Converter<Map<String, Object>, V>) yamlConvertor;
    }

    private static final KafkaPropertiesConverter<Map<String, Object>> yamlConvertor = configs -> {
        final KafkaProperties kafkaProperties = new KafkaProperties();
        final String cluster = String.valueOf(configs.get("cluster"));
        final Map<String, Object> bootstrap = (LinkedHashMap<String, Object>) configs.get("bootstrap");
        kafkaProperties.setBootstrapServers(String.valueOf(bootstrap.getOrDefault("servers", "localhost:9092")));
        kafkaProperties.setClusterName(cluster);
        return kafkaProperties.buildProperties();
    };

}
