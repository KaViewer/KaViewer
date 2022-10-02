package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.persist.PersistKafkaPropertiesHandler;
import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.app.service.resolver.KafkaPropertiesConvert;
import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class KaViewerEnvScanner {
    private final KafkaApplicationSetupService kafkaApplicationSetupService;
    private final KaViewerConfiguration kaViewerConfiguration;
    private final KafkaPropertiesConvert kafkaPropertiesConvert;
    private final Environment environment;
    private final List<PropertySourceLoader> propertySourceLoaders;
    private final PersistKafkaPropertiesHandler persistKafkaPropertiesHandler;

    @PostConstruct
    public void scanEnv() {
        final String config = Binder.get(environment).bind(CommonConstant.KAVIEWER_CONFIG_FILEPATH, String.class).orElseGet(() -> "");
        log.info("Scan KAVIEWER_CONFIG_FILEPATH: [{}]", config);
        KaViewerConfiguration kaViewerConfiguration = this.kaViewerConfiguration;
        try {
            if (StringUtils.isNotEmpty(config)) {
                kaViewerConfiguration = load(CommonConstant.KAVIEWER, KaViewerConfiguration.class, config);
                Assert.notNull(kaViewerConfiguration, "Config file is invalid to bind");
            }
            this.kaViewerConfiguration.renew(kaViewerConfiguration);
            log.info("Current working in [{}] mode", kaViewerConfiguration.getMode());

            final List<KafkaProperties> persistKafkaProperties = Optional.ofNullable(persistKafkaPropertiesHandler.load()).orElseGet(ArrayList::new);
            final var persistedClusters = persistKafkaProperties.stream().map(KafkaProperties::getClusterName).collect(Collectors.toSet());

            final var kaViewerKafkaConfiguration = kaViewerConfiguration.getConnections();

            if (!CollectionUtils.isEmpty(kaViewerKafkaConfiguration)) {
                kaViewerKafkaConfiguration.stream().filter(KaViewerKafkaConfiguration::valid)
                        .filter(it -> !persistedClusters.contains(it.getCluster()))
                        .forEach(it -> {
                            final KafkaProperties kafkaProperties = kafkaPropertiesConvert.convert(it);
                            if (!persistKafkaProperties.contains(kafkaProperties)) {
                                persistKafkaProperties.add(kafkaProperties);
                            }
                        });
            }
            bindSetUpKafka(persistKafkaProperties);

        } catch (Exception e) {
            log.error("scanEnv error:", e);
            e.printStackTrace();
        }
    }

    private void bindSetUpKafka(List<KafkaProperties> kafkaProperties) {
        kafkaProperties.stream().filter(Objects::nonNull).forEach(kafkaApplicationSetupService::setUp);
    }

    public <T> T load(String name, Class<T> target, String file) throws IOException {
//        final Resource resource = new FileSystemResourceLoader().getResource("file:/example/example-config.yaml");
        final PropertySourceLoader sourceLoader = this.propertySourceLoaders
                .stream()
                .filter(propertySourceLoader -> Arrays.stream(propertySourceLoader.getFileExtensions())
                        .anyMatch(file::endsWith))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found matched source loader"));
        final List<PropertySource<?>> config = sourceLoader.load(name, new FileSystemResource(file));
        final Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(config);
        final ConfigurationPropertySource next = from.iterator().next();
        final Binder binder = new Binder(next);
        final BindResult<T> bind = binder.bind(name, Bindable.of(target));
        return bind.orElseGet(() -> null);
    }
}
