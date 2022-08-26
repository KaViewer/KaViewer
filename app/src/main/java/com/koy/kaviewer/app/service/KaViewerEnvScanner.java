package com.koy.kaviewer.app.service;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.app.service.resolver.KafkaPropertiesConvert;
import com.koy.kaviewer.common.constant.CommonConstant;
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
import org.springframework.util.Assert;
import org.webjars.NotFoundException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class KaViewerEnvScanner {
    private static final String KAVIEWER_CONFIG = "kaviewer.config.filepath";
    private final KafkaApplicationSetupService kafkaApplicationSetupService;
    private final KaViewerConfiguration kaViewerConfiguration;
    private final KafkaPropertiesConvert kafkaPropertiesConvert;
    private final Environment environment;
    private final List<PropertySourceLoader> propertySourceLoaders;


    @PostConstruct
    public void scanEnv() {
        final String config = Binder.get(environment).bind(KAVIEWER_CONFIG, String.class).orElseGet(() -> "");
        KaViewerConfiguration kaViewerConfiguration = this.kaViewerConfiguration;
        try {
            if (StringUtils.isNotEmpty(config)) {
                kaViewerConfiguration = load(CommonConstant.KAVIEWER, KaViewerConfiguration.class, config);
                Assert.notNull(kaViewerConfiguration, "Config file is invalid to bind.");
            }
            this.kaViewerConfiguration.renew(kaViewerConfiguration);

            bindSetUpKafka(kaViewerConfiguration.getKafka());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void bindSetUpKafka(KaViewerKafkaConfiguration kafkaConfiguration) {
        if (Objects.isNull(kafkaConfiguration) || kafkaConfiguration.invalid()) {
            return;
        }
        kafkaApplicationSetupService.setUp(kafkaPropertiesConvert.convert(kafkaConfiguration));
    }

    @SneakyThrows
    public <T> T load(String name, Class<T> target, String file) {
//        final Resource resource = new FileSystemResourceLoader().getResource("file:/example/example-config.yaml");
        final PropertySourceLoader sourceLoader = this.propertySourceLoaders
                .stream()
                .filter(propertySourceLoader -> Arrays.stream(propertySourceLoader.getFileExtensions())
                        .anyMatch(file::endsWith))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not Found Matched Source Loader"));
        final List<PropertySource<?>> config = sourceLoader.load(name, new FileSystemResource(file));
        final Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(config);
        final ConfigurationPropertySource next = from.iterator().next();
        final Binder binder = new Binder(next);
        final BindResult<T> bind = binder.bind(name, Bindable.of(target));
        return bind.orElseGet(() -> null);
    }
}
