package com.koy.kaviewer.common.configuration;

import com.koy.kaviewer.common.toggle.KaViewerMode;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Data
@ConfigurationProperties("kaviewer")
public class KaViewerConfiguration implements FactoryBean<KaViewerConfiguration> {
    @ToString.Exclude
    private KaViewerConfiguration INSTANCE = this;
    private KaViewerMode mode = KaViewerMode.LITE;
    KaViewerAppConfiguration app;
    KaViewerWebConfiguration web;
    KaViewerKafkaConfiguration kafka;

    @Override
    public KaViewerConfiguration getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return KaViewerConfiguration.class;
    }

    public void updateINSTANCE(KaViewerConfiguration kaViewerConfiguration) {
        INSTANCE = kaViewerConfiguration;
    }
}
