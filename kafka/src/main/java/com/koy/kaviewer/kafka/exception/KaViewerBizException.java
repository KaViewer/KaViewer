package com.koy.kaviewer.kafka.exception;

public class KaViewerBizException extends RuntimeException {
    private final ErrorMsg errorMsg;

    public KaViewerBizException(ErrorMsg message) {
        super(message.getMsg());
        this.errorMsg = message;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public static KaViewerBizException of(ErrorMsg errorMsg) {
        return new KaViewerBizException(errorMsg);
    }
}
