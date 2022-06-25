package com.koy.kaviewer.rest;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.share.RequestContextManagement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;


@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.rest"})
@EnableWebMvc
@EnableOpenApi
public class KaViewerRestApplication implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(KaViewerRestApplication.class);
    private static ApplicationContext rest;
    private static ApplicationContext parent;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KaViewerRestApplication.rest = applicationContext;
        KaViewerRestApplication.parent = applicationContext.getParent();
    }

    public static <T> T getBean(Class<T> clz) {
        T target = null;
        try {
            final String cluster = RequestContextManagement.getCluster();
            if (StringUtils.isNotEmpty(cluster)) {
                LOGGER.info("Do get bean on cluster :[{}]", cluster);
                target = KafkaApplication.getKafkaApplicationBean(cluster, clz);
            } else {
                target = KaViewerRestApplication.parent.getBean(clz);
            }
        } catch (BeansException ignore) {
            target = KaViewerRestApplication.rest.getBean(clz);
        }
        return target;
    }

    public static ApplicationContext getRest() {
        return rest;
    }
}
