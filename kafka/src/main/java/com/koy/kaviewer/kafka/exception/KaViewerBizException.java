package com.koy.kaviewer.kafka.exception;

public class KaViewerBizException extends RuntimeException {
    private final ErrorMsg errorMsg;
    private final Throwable cause;

    public KaViewerBizException(ErrorMsg errorMsg, Throwable cause) {
        super(errorMsg.getMsg(), cause);
        this.errorMsg = errorMsg;
        this.cause = cause;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public static KaViewerBizException of(ErrorMsg errorMsg) {
        return new KaViewerBizException(errorMsg, null);
    }

    public static KaViewerBizException of(ErrorMsg errorMsg, Throwable cause) {
        return new KaViewerBizException(errorMsg, cause);
    }
}
