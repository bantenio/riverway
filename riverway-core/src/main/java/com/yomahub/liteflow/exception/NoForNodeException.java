package com.yomahub.liteflow.exception;

public class NoForNodeException extends RuntimeException {
    public NoForNodeException() {
    }

    public NoForNodeException(String message) {
        super(message);
    }

    public NoForNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoForNodeException(Throwable cause) {
        super(cause);
    }

    public NoForNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
