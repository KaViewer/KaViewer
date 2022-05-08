package com.koy.kaviewer.rest;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.rest"})
public class KaViewerRestApplication implements ApplicationContextAware {
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
            target = KaViewerRestApplication.parent.getBean(clz);
        } catch (BeansException ignore) {
            target = KaViewerRestApplication.rest.getBean(clz);
        }
        return target;
    }

}
