package com.koy.kaviewer.common.share;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class RequestContextManagement {
    private static final ThreadLocal<RequestContext> requestContext = new InheritableThreadLocal<>();


    public static void create(RequestContext ctx) {
        requestContext.set(ctx);
    }

    public static void reset() {
        requestContext.remove();
    }

    public static String getCluster() {
        return Optional.ofNullable(requestContext.get()).orElseGet(() -> new RequestContext(StringUtils.EMPTY)).getClusterName();
    }


    public static class RequestContext {
        private String clusterName;

        public RequestContext(String clusterName) {
            this.clusterName = clusterName;
        }

        public String getClusterName() {
            return clusterName;
        }
    }


}
