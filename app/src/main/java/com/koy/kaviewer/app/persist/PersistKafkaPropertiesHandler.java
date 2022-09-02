package com.koy.kaviewer.app.persist;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersistKafkaPropertiesHandler implements ApplicationListener<ContextClosedEvent>, Persistent {
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/kaviewer.factories";

    private static final Map<String, List<Persistent>> persistHandlers = new ConcurrentReferenceHashMap<>();

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // ignore child kafka close event
        // KaViewerApplication.getRoot()
        log.warn("Receive ApplicationCloseEvent on Application: [{}]", event.getApplicationContext().getId());
    }


    @SneakyThrows
    @PostConstruct
    public void scan() {
        Enumeration<URL> urls = getClassloader().getResources(FACTORIES_RESOURCE_LOCATION);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            UrlResource resource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                String factoryTypeName = ((String) entry.getKey()).trim();
                String[] factoryImplementationNames =
                        StringUtils.commaDelimitedListToStringArray((String) entry.getValue());
                final List<Persistent> handlers = new ArrayList<>();
                for (String factoryImplementationName : factoryImplementationNames) {

                    final boolean persistHandler = Class.forName(factoryTypeName).isAssignableFrom(Persistent.class);
                    if (persistHandler) {
                        final Class<?> factoryImplementationClass = ClassUtils.forName(factoryImplementationName, getClassloader());
                        final Persistent instance = (Persistent) ReflectionUtils.accessibleConstructor(factoryImplementationClass).newInstance();
                        handlers.add(instance);
                    }
                }
                persistHandlers.putIfAbsent(factoryTypeName, handlers);
            }
        }
    }

    @Override
    public List<KafkaProperties> load() {
        if (CollectionUtils.isEmpty(persistHandlers)) {
            return new ArrayList<>();
        }

        return persistHandlers.values().stream().flatMap(Collection::stream)
                .map(Persistent::load).flatMap(Collection::parallelStream).collect(Collectors.toList());
    }


    @PreDestroy
    public void doPersist() {
        log.info("PreDestroy do persist kafkaProperties.");
        final List<KafkaProperties> kafkaProperties = KafkaApplicationHolder.getKafkaProperties();
        if (kafkaProperties.isEmpty()) {
            return;
        }

        persist(kafkaProperties);
    }

    @Override
    @SneakyThrows
    public void persist(List<KafkaProperties> kafkaProperties) {
        if (CollectionUtils.isEmpty(persistHandlers)) {
            return;
        }
        persistHandlers.values().stream().flatMap(Collection::stream)
                .forEach(instance -> instance.persist(kafkaProperties));
    }

}