package com.yomahub.liteflow.builder;

public class LiteFlowParseException extends RuntimeException {
    public LiteFlowParseException() {
    }

    public LiteFlowParseException(String message) {
        super(message);
    }

    public LiteFlowParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiteFlowParseException(Throwable cause) {
        super(cause);
    }

    public LiteFlowParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
