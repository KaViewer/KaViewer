package com.koy.kaviewer.common.exception;

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
        return new KaViewerBizException(errorMsg, new IllegalStateException());
    }

    public static KaViewerBizException of(Throwable cause) {
        return new KaViewerBizException(ErrorMsg.RUNTIME_ERROR, cause);
    }

    public static KaViewerBizException of(ErrorMsg errorMsg, Throwable cause) {
        return new KaViewerBizException(errorMsg, cause);
    }
}
