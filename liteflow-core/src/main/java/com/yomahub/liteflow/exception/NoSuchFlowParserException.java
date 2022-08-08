package com.yomahub.liteflow.exception;

import com.yomahub.liteflow.builder.LiteFlowParseException;

public class NoSuchFlowParserException extends LiteFlowParseException {
    public NoSuchFlowParserException() {
    }

    public NoSuchFlowParserException(String message) {
        super(message);
    }

    public NoSuchFlowParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchFlowParserException(Throwable cause) {
        super(cause);
    }

    public NoSuchFlowParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
