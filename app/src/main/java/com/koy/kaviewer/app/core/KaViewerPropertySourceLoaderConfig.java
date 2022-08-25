package com.koy.kaviewer.app.core;

import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaViewerPropertySourceLoaderConfig {

    @Bean
    public YamlPropertySourceLoader yamlPropertySourceLoader() {
        return new YamlPropertySourceLoader();
    }

    @Bean
    public PropertiesPropertySourceLoader propertiesPropertySourceLoader() {
        return new PropertiesPropertySourceLoader();
    }
}
