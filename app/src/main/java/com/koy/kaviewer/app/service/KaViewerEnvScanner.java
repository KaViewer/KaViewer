package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.configuration.KaViewerConfiguration;
import com.koy.kaviewer.app.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.app.service.resolver.KafkaPropertiesConvert;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class KaViewerEnvScanner {
    private static final String KAVIEWER_CONFIG = "kaviewer.config.file";
    private final KafkaApplicationSetupService kafkaApplicationSetupService;
    private final KaViewerConfiguration kaViewerConfiguration;
    private final KafkaPropertiesConvert kafkaPropertiesConvert;
    private final Environment environment;
    private final List<PropertySourceLoader> propertySourceLoaders;


    @PostConstruct
    public void scanEnv() {
        final String config = Binder.get(environment).bind(KAVIEWER_CONFIG, String.class).orElseGet(() -> "");
        KaViewerConfiguration kaViewerConfiguration = this.kaViewerConfiguration;
        if (StringUtils.isNotEmpty(config)) {
            kaViewerConfiguration = load(KAVIEWER_CONFIG, KaViewerConfiguration.class, config);
        }
        try {
            final KaViewerKafkaConfiguration kafkaConfiguration = kaViewerConfiguration.getKafka();
            kafkaApplicationSetupService.setUp(kafkaPropertiesConvert.convert(kafkaConfiguration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public <T> T load(String name, Class<T> target, String file) {
//        final Resource resource = new FileSystemResourceLoader().getResource("file:/tmp/config.yaml");
        final PropertySourceLoader sourceLoader = this.propertySourceLoaders.stream().filter(propertySourceLoader -> Arrays.stream(propertySourceLoader.getFileExtensions()).anyMatch(file::endsWith)).findFirst().orElseThrow(() -> new NotFoundException("Not Found Matched Source Loader"));
        final List<PropertySource<?>> config = sourceLoader.load(name, new FileSystemResource(file));
        final Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(config);
        final ConfigurationPropertySource next = from.iterator().next();
        final Binder binder = new Binder(next);
        final BindResult<T> bind = binder.bind(name, Bindable.of(target));
        return bind.orElseGet(() -> null);
    }
}
