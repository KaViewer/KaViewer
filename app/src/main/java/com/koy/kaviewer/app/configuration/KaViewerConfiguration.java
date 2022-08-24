package com.koy.kaviewer.app.configuration;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Data
@ConfigurationProperties("kaviewer")
public class KaViewerConfiguration {
    KaViewerAppConfiguration app;
    KaViewerWebConfiguration web;
    KaViewerKafkaConfiguration kafka;
}
