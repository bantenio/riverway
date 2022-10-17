package com.yomahub.liteflow.exception;

public class NoWhileNodeException extends RuntimeException {
    public NoWhileNodeException() {
    }

    public NoWhileNodeException(String message) {
        super(message);
    }

    public NoWhileNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoWhileNodeException(Throwable cause) {
        super(cause);
    }

    public NoWhileNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
