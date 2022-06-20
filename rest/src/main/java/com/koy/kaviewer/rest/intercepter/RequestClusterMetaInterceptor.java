package com.koy.kaviewer.rest.intercepter;

import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import com.koy.kaviewer.kafka.share.RequestContextManagement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestClusterMetaInterceptor implements WebMvcConfigurer, HandlerInterceptor {
    private static final String CLUSTER_HEADER_KEY = "k-cluster";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .excludePathPatterns("/api/*/cluster")
                .addPathPatterns("/api/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clusterName = request.getHeader(CLUSTER_HEADER_KEY);
        if (StringUtils.isEmpty(clusterName)) {
            throw KaViewerBizException.of(ErrorMsg.NO_CLUSTER_META);
        }
        RequestContextManagement.create(new RequestContextManagement.RequestContext(clusterName));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestContextManagement.reset();
    }
}
