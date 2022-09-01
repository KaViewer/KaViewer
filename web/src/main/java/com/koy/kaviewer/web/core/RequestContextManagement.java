package com.koy.kaviewer.web.core;

import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestContextManagement {
    private static final ThreadLocal<RequestContext> requestContext = new InheritableThreadLocal<>();


    public static void create(RequestContext ctx) {
        requestContext.set(ctx);
    }

    public static ThreadLocal<RequestContext> getRequestContext() {
        return requestContext;
    }

    public static void reset() {
        requestContext.remove();
    }

    public static String getCluster() {
        return requestContext.get().getClusterName();
    }


    @Data
    @NoArgsConstructor
    public static class RequestContext {
        private String clusterName;
    }


}
