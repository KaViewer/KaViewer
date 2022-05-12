package com.koy.kaviewer.rest.intercepter;

import com.koy.kaviewer.rest.core.RequestContextManagement;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements WebMvcConfigurer, HandlerInterceptor {
    private static final String CLUSTER_HEADER_KEY = "k-cluster";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/api/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clusterName = request.getHeader(CLUSTER_HEADER_KEY);
        RequestContextManagement.create(new RequestContextManagement.RequestContext(clusterName));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestContextManagement.reset();
    }
}
