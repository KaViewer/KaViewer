package com.koy.kaviewer.kafka.exception;

public enum ErrorMsg {
    INIT_ERROR("Init KaViewer Error."),
    CLUSTER_EXIST("Cluster already exist."),
    ;

    private final String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
