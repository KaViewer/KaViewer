package com.koy.kaviewer.app.core.event;

public abstract class RestEvent implements Event<RestEvent> {

    private String clusterName;

    @Override
    public RestEvent call() throws Exception {
        return null;
    }

    abstract String getClusterName();
}
