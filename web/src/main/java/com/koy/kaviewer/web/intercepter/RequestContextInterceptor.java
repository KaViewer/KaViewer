package com.koy.kaviewer.web.intercepter;

import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.web.core.RequestContextManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String method = request.getMethod();
        if (HttpMethod.OPTIONS.matches(method)) {
            log.info("Options request, pass");
            return true;
        }
        final String url = request.getRequestURI();
        final String queryString = String.valueOf(request.getQueryString());
        final String cluster = String.valueOf(request.getHeader(CommonConstant.KAVIEWER_CLUSTER_HEADER_KEY));
        log.info("Receive request method: [{}], request url:[{}], queryString:[{}], cluster:[{}]", method, url, queryString, cluster);
        RequestContextManagement.create(new RequestContextManagement.RequestContext());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestContextManagement.reset();
    }
}
