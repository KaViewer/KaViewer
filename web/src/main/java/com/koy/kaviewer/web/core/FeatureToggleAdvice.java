package com.koy.kaviewer.web.core;

import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.ToggleResolver;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureToggleAdvice {
    @Autowired
    ToggleResolver toggleResolver;

    @Pointcut("@annotation(com.koy.kaviewer.common.toggle.FeatureToggle)")
    public void featureTogglePointcut() {
    }

    @Around("featureTogglePointcut()")
    @SneakyThrows
    public Object featureToggle(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final FeatureToggle featureToggle = methodSignature.getMethod().getAnnotation(FeatureToggle.class);
//        final ToggleResolver toggleResolver = KaViewerWebApplication.getBean(ToggleResolver.class);
        final boolean enable = toggleResolver.enable(featureToggle);
        Object proceed = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (enable) {
            proceed = pjp.proceed();
        }
        return proceed;
    }
}
