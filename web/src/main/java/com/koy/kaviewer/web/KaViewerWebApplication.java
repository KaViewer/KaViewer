package com.koy.kaviewer.web;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.share.RequestContextManagement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.web"})
@EnableWebMvc
public class KaViewerWebApplication implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(KaViewerWebApplication.class);
    private static ApplicationContext rest;
    private static ApplicationContext parent;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KaViewerWebApplication.rest = applicationContext;
        KaViewerWebApplication.parent = applicationContext.getParent();
    }

    public static KaViewerConfiguration getKaViewerConfiguration() {
        return KaViewerWebApplication.parent.getBean(KaViewerConfiguration.class).getObject();
    }

    public static <T> T getBean(String cluster, Class<T> clz) {
        T target = null;
        try {
            if (StringUtils.isNotEmpty(cluster)) {
                LOGGER.info("Do get bean on cluster :[{}]", cluster);
                target = KafkaApplicationHolder.getKafkaApplicationBean(cluster, clz);
            } else {
                target = KaViewerWebApplication.parent.getBean(clz);
            }
        } catch (BeansException ignore) {
            target = KaViewerWebApplication.rest.getBean(clz);
        }
        return target;
    }


    public static <T> T getBean(Class<T> clz) {
        final String cluster = RequestContextManagement.getCluster();
        return getBean(cluster, clz);
    }

    public static ApplicationContext getRest() {
        return rest;
    }
}
