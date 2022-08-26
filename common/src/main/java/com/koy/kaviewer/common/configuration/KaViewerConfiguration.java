package com.koy.kaviewer.common.configuration;

import com.koy.kaviewer.common.toggle.KaViewerMode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Data
@ConfigurationProperties("kaviewer")
public class KaViewerConfiguration implements FactoryBean<KaViewerConfiguration> {
    @ToString.Exclude
    private KaViewerConfiguration INSTANCE = this;
    private KaViewerMode mode = KaViewerMode.LITE;
    private KaViewerAppConfiguration app;
    private KaViewerWebConfiguration web;
    private KaViewerKafkaConfiguration kafka;
    private Map<String, Boolean> toggle = Map.of();


    @SneakyThrows
    @Override
    public KaViewerConfiguration getObject() {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return KaViewerConfiguration.class;
    }

    public void renew(KaViewerConfiguration kaViewerConfiguration) {
        INSTANCE = kaViewerConfiguration;
    }

    // topic.create = true  -> create=true
    public Map<String, Boolean> filter(String key) {
        return toggle.entrySet().stream().filter(it -> {
            final String k = it.getKey();
            return k.toLowerCase(Locale.ROOT).startsWith(key.toLowerCase(Locale.ROOT));

        }).collect(
                Collectors.toMap(
                        it -> it.getKey().split("\\.")[1],
                        Map.Entry::getValue,
                        (t1, t2) -> t2)
        );
    }
}
