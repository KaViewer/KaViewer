package com.koy.kaviewer.rest.core;

public class RequestContextManagement {
    private static final ThreadLocal<RequestContext> requestContext = new InheritableThreadLocal<>();


    public static void create(RequestContext ctx) {
        requestContext.set(ctx);
    }

    public static void reset() {
        requestContext.remove();
    }

    public static String getCluster() {
        return requestContext.get().getClusterName();
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
