package com.koy.kaviewer.shell.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * use this annotation to replace @ShellComponent for KaViewer commands, only the Build-In commands use StandardMethodTargetRegistrar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface KaViewerShellComponent {

    String value() default "";
}