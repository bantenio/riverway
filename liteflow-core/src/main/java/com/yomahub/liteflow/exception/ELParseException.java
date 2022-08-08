package com.yomahub.liteflow.exception;

import com.yomahub.liteflow.builder.LiteFlowParseException;

public class ELParseException extends LiteFlowParseException {
    public ELParseException() {
    }

    public ELParseException(String message) {
        super(message);
    }

    public ELParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ELParseException(Throwable cause) {
        super(cause);
    }

    public ELParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
