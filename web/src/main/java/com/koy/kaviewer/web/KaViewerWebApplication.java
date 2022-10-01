package com.koy.kaviewer.web;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.web.core.RequestContextManagement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.web"})
@EnableWebMvc
@Slf4j
public class KaViewerWebApplication implements ApplicationContextAware {
    private static ApplicationContext rest;
    private static ApplicationContext parent;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KaViewerWebApplication.rest = applicationContext;
        KaViewerWebApplication.parent = applicationContext.getParent();
    }

    public static KaViewerConfiguration getKaViewerConfiguration() {
        return KaViewerWebApplication.parent.getBean(KaViewerConfiguration.class).getINSTANCE();
    }

    public static <T> T getBean(String cluster, Class<T> clz) {
        T target = null;
        try {
            if (StringUtils.isNotEmpty(cluster)) {
                log.info("Start do get bean for cluster :[{}]", cluster);
                target = KafkaApplicationHolder.getKafkaApplicationBean(cluster, clz);
            } else {
                target = KaViewerWebApplication.parent.getBean(clz);
            }
        } catch (BeansException ignore) {
            log.error("Error while getting bean in app application context with clz:[{}], try to get at rest ctx", clz.getSimpleName());
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
