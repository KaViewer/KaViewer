package com.koy.kaviewer.app.configuration;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ToString
@Data
@ConfigurationProperties("kaviewer")
public class KaViewerConfiguration {
    String name;
    KaViewerAppConfiguration app;
    KaViewerWebConfiguration web;
    KaViewerKafkaConfiguration kafka;

    @PostConstruct
    void test(){
        System.out.println(this);
    }
}
