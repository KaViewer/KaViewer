package com.koy.kaviewer.kafka.exception;

public enum ErrorMsg {
    CLUSTER_EXIST("Cluster already exist.");

    private String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
