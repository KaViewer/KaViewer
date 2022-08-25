package com.koy.kaviewer.app;


import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.web.KaViewerWebApplication;
import org.springframework.beans.BeansException;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import java.util.Objects;

@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.app"})
@EnableConfigurationProperties(KaViewerConfiguration.class)
public class KaViewerApplication implements ApplicationContextAware {
    private static ApplicationContext root;
    private static String[] args0;

    public static void main(String[] args) {
        args0 = args;
        SpringApplicationBuilder kaViewerApplicationBuilder =
                new SpringApplicationBuilder(KaViewerApplication.class)
                        .bannerMode(Banner.Mode.LOG)
                        .web(WebApplicationType.NONE);
        final ConfigurableApplicationContext app = kaViewerApplicationBuilder.run(args);
        app.setId(KaViewerApplication.class.getSimpleName());

        final ConfigurableApplicationContext web = kaViewerApplicationBuilder.child(KaViewerWebApplication.class)
                .profiles("web")
                .bannerMode(Banner.Mode.OFF)
                .run(args);
        web.setId(KaViewerWebApplication.class.getSimpleName());
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(applicationContext.getParent())) {
            KaViewerApplication.root = applicationContext;
        }
    }

    public static ApplicationContext getRoot() {
        return root;
    }

    public static String[] getArgs() {
        return args0;
    }
}
