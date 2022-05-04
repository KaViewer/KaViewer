package com.koy.kaviewer;


import com.koy.kaviewer.app.KafkaApplication;
import com.koy.kaviewer.app.RestApplication;
import com.koy.kaviewer.service.KafkaSetUpService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.util.Objects;


@SpringBootApplication(scanBasePackages = "com.koy.kaviewer.service")
public class KaViewerApplication implements ApplicationContextAware {
    @Autowired
    KafkaSetUpService kafkaSetUpService;
    private static ApplicationContext root;
    private static String[] args0;

    public static void main(String[] args) {
        args0 = args;
        SpringApplicationBuilder parentBuilder =
                new SpringApplicationBuilder(KaViewerApplication.class)
                        .web(WebApplicationType.NONE);
        final ConfigurableApplicationContext parent = parentBuilder.run(args);
        final ConfigurableApplicationContext rest = parentBuilder.child(RestApplication.class)
                .properties("spring.application.name=KaViewer-rest")
                .profiles("rest")
                .run(args);
    }

    @PostConstruct
    public void setUp() {
        kafkaSetUpService.setUp((ConfigurableApplicationContext) root, args0);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        System.out.println(applicationContext.getApplicationName());
        System.out.println(applicationContext.getId());
        if (Objects.isNull(applicationContext.getParent())) {
            System.out.println("get current application ctx is  " + applicationContext);
            KaViewerApplication.root = applicationContext;
        }
    }

    public static ApplicationContext getRoot() {
        return root;
    }
}
