package com.yomahub.liteflow.exception;

import com.yomahub.liteflow.builder.LiteFlowParseException;

public class NotSupportParseWayException extends LiteFlowParseException {
    public NotSupportParseWayException() {
    }

    public NotSupportParseWayException(String message) {
        super(message);
    }

    public NotSupportParseWayException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportParseWayException(Throwable cause) {
        super(cause);
    }

    public NotSupportParseWayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
