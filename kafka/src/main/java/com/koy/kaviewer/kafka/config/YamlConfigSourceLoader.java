package com.koy.kaviewer.kafka.config;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@Component
public class YamlConfigSourceLoader implements ConfigSourceLoader<Map<String, Object>, String> {
    @Override
    public Map<String, Object> load(String source) throws FileNotFoundException {
        final Yaml yaml = new Yaml();
        return yaml.load(new FileInputStream(source));
    }

    @Override
    public boolean support(String fileName) {
        return fileName.endsWith(".yaml") || fileName.endsWith(".yml");
    }
}
