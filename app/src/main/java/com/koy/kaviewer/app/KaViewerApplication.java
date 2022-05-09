package com.koy.kaviewer.app;


import com.koy.kaviewer.rest.KaViewerRestApplication;
import org.springframework.beans.BeansException;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import java.util.Objects;

@SpringBootApplication(scanBasePackages = {
        "com.koy.kaviewer.app", "com.koy.kaviewer.kafka.core",
})
public class KaViewerApplication implements ApplicationContextAware {
    private static ApplicationContext root;
    private static String[] args0;

    public static void main(String[] args) {
        args0 = args;
        SpringApplicationBuilder parentBuilder =
                new SpringApplicationBuilder(KaViewerApplication.class)
                        .bannerMode(Banner.Mode.LOG)
                        .web(WebApplicationType.NONE);
        parentBuilder.run(args);
        parentBuilder.child(KaViewerRestApplication.class)
                .properties("spring.application.name=KaViewer-rest")
                .profiles("rest")
                .bannerMode(Banner.Mode.OFF)
                .run(args);
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
