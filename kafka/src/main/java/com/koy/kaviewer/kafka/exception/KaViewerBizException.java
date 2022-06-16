package com.koy.kaviewer.kafka.exception;

public class KaViewerBizException extends RuntimeException {
    public KaViewerBizException(ErrorMsg message) {
        super(message.getMsg());
    }
}
