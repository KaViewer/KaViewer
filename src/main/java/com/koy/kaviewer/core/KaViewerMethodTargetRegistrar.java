package com.koy.kaviewer.core;

import com.koy.kaviewer.core.annotation.KaViewerShellComponent;
import com.koy.kaviewer.core.annotation.KaViewerShellGroupCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.Command;
import org.springframework.shell.ConfigurableCommandRegistry;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.MethodTargetRegistrar;
import org.springframework.shell.Utils;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class KaViewerMethodTargetRegistrar implements MethodTargetRegistrar {
    private final ApplicationContext applicationContext;
    private final Map<String, MethodTarget> commands = new HashMap<>();

    public KaViewerMethodTargetRegistrar(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void register(ConfigurableCommandRegistry registry) {
        Map<String, Object> commandBeans = applicationContext.getBeansWithAnnotation(KaViewerShellComponent.class);
        for (Object bean : commandBeans.values()) {
            Class<?> clazz = bean.getClass();
            ReflectionUtils.doWithMethods(clazz, method -> {
                ShellMethod shellMapping = method.getAnnotation(ShellMethod.class);
                String[] keys = shellMapping.key();
                if (keys.length == 0) {
                    keys = new String[]{Utils.unCamelify(method.getName())};
                }
                String group = getOrInferGroup(method);
                String kaViewerGroupCommand = getKaViewerGroupCommand(method);
                for (String key : keys) {
                    // default all the methods are available.
                    MethodTarget target = new MethodTarget(method, bean, new Command.Help(shellMapping.value(), group), Availability::available);
                    String newKey = kaViewerGroupCommand + "#" + key;
                    registry.register(newKey, target);
                    commands.put(newKey, target);
                }
            }, method -> method.getAnnotation(ShellMethod.class) != null);
        }
    }

    private String getKaViewerGroupCommand(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        KaViewerShellGroupCommand classAnn = AnnotationUtils.getAnnotation(clazz, KaViewerShellGroupCommand.class);
        if (classAnn != null && !classAnn.value().equals(ShellCommandGroup.INHERIT_AND_INFER)) {
            return classAnn.value();
        }
        return StringUtils.arrayToDelimitedString(clazz.getSimpleName().split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"), " ");
    }

    private String getOrInferGroup(Method method) {
        ShellMethod methodAnn = AnnotationUtils.getAnnotation(method, ShellMethod.class);
        assert methodAnn != null;
        if (!methodAnn.group().equals(ShellMethod.INHERITED)) {
            return methodAnn.group();
        }
        Class<?> clazz = method.getDeclaringClass();
        ShellCommandGroup classAnn = AnnotationUtils.getAnnotation(clazz, ShellCommandGroup.class);
        if (classAnn != null && !classAnn.value().equals(ShellCommandGroup.INHERIT_AND_INFER)) {
            return classAnn.value();
        }
        ShellCommandGroup packageAnn = AnnotationUtils.getAnnotation(clazz.getPackage(), ShellCommandGroup.class);
        if (packageAnn != null && !packageAnn.value().equals(ShellCommandGroup.INHERIT_AND_INFER)) {
            return packageAnn.value();
        }
        // Shameful copy/paste from https://stackoverflow.com/questions/7593969/regex-to-split-camelcase-or-titlecase-advanced
        return StringUtils.arrayToDelimitedString(clazz.getSimpleName().split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"), " ");
    }
}
