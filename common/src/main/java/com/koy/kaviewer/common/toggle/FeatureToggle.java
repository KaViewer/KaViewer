package com.koy.kaviewer.common.toggle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FeatureToggle {
    Class<? extends Toggle<?>> toggleGroup();

    Operations operation();

}
