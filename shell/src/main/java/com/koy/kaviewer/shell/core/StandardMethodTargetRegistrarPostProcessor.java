package com.koy.kaviewer.shell.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.shell.MethodTargetRegistrar;
import org.springframework.shell.legacy.LegacyMethodTargetRegistrar;
import org.springframework.shell.standard.StandardMethodTargetRegistrar;
import org.springframework.stereotype.Component;

/**
 * Do the trick to make it happen that using KaViewerConfigurableCommandRegistry to resolve parameters with converter.
 */
@Component
public class StandardMethodTargetRegistrarPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof StandardMethodTargetRegistrar || bean instanceof LegacyMethodTargetRegistrar) {
            KaViewerConfigurableCommandRegistry kaViewerConfigurableCommandRegistry = new KaViewerConfigurableCommandRegistry();
            MethodTargetRegistrar registrar = (MethodTargetRegistrar) bean;
            registrar.register(kaViewerConfigurableCommandRegistry);
        }
        return null;
    }
}
