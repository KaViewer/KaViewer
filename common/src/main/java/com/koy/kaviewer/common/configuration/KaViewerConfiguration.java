package com.koy.kaviewer.common.configuration;

import com.koy.kaviewer.common.toggle.KaViewerMode;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Data
@ConfigurationProperties("kaviewer")
public class KaViewerConfiguration {
    @ToString.Exclude
    private KaViewerConfiguration INSTANCE = this;
    private KaViewerMode mode = KaViewerMode.LITE;
    private KaViewerAppConfiguration app;
    private KaViewerWebConfiguration web;
    private List<KaViewerKafkaConfiguration> connections = List.of();
    private Map<String, Boolean> toggle = Map.of();

    public void renew(KaViewerConfiguration kaViewerConfiguration) {
        INSTANCE = kaViewerConfiguration;
    }

    // topic.create = true  -> create=true
    public Map<String, Boolean> filter(String key) {
        return getToggle().entrySet().stream().filter(it -> {
            final String k = it.getKey();
            return k.toLowerCase(Locale.ROOT).startsWith(key.toLowerCase(Locale.ROOT));

        }).collect(
                Collectors.toMap(
                        it -> it.getKey().split("\\.")[1],
                        Map.Entry::getValue,
                        (t1, t2) -> t2)
        );
    }

    public KaViewerAppConfiguration getApp() {
        return INSTANCE.app;
    }

    public KaViewerWebConfiguration getWeb() {
        return INSTANCE.web;
    }

    public List<KaViewerKafkaConfiguration> getConnections() {
        return INSTANCE.connections;
    }

    public Map<String, Boolean> getToggle() {
        return INSTANCE.toggle;
    }

    public KaViewerMode getMode() {
        return INSTANCE.mode;
    }
}
