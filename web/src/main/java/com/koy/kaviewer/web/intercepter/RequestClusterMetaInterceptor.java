package com.koy.kaviewer.web.intercepter;

import com.koy.kaviewer.common.exception.ErrorMsg;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import com.koy.kaviewer.common.share.RequestContextManagement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestClusterMetaInterceptor implements HandlerInterceptor {
    private static final String CLUSTER_HEADER_KEY = "k-cluster";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String clusterName = request.getHeader(CLUSTER_HEADER_KEY);
        if (StringUtils.isEmpty(clusterName)) {
            throw KaViewerBizException.of(ErrorMsg.NO_CLUSTER_META);
        }
        RequestContextManagement.getRequestContext().get().setClusterName(clusterName);
        return true;
    }

}
