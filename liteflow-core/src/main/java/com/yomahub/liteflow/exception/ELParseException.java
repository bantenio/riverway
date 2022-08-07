package com.yomahub.liteflow.exception;

import com.yomahub.liteflow.builder.LiteFlowParseException;

public class ELParseException extends LiteFlowParseException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常信息
     */
    private String message;

    public ELParseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
