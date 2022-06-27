package com.koy.kaviewer.kafka.exception;

public enum ErrorMsg {
    NO_CLUSTER_META("No Specific Cluster Info."),
    NO_CLUSTER_FOUND("No Specific Cluster Found."),
    INIT_ERROR("Init KaViewer Error."),
    CLUSTER_EXIST("Cluster already exist."),
    INTERNAL_ERROR("Internal Error."),
    RUNTIME_ERROR("Runtime Error."),
    ;

    private final String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
