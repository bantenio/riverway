package com.yomahub.liteflow.example.exception;

import com.yomahub.liteflow.example.result.Result;

public class BaseBusinessException extends BaseException {
    public BaseBusinessException() {
        this(Result.UNKNOWN);
    }

    public BaseBusinessException(Result errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }

    public BaseBusinessException(Result errorCode, String customMsg) {
        super(errorCode.getCode(), customMsg);
    }

    public BaseBusinessException(Result errorCode, Throwable cause) {
        super(errorCode.getCode(), errorCode.getMessage(), cause);
    }

    public BaseBusinessException(Result errorCode, String customMsg, Throwable cause) {
        super(errorCode.getCode(), customMsg, cause);
    }

    public String toString() {
        return String.format("BusinessException code:%s,msg:%s", this.getCode(), this.getMsg());
    }

    public String getMessage() {
        return String.format("code:%s,msg:%s", this.getCode(), this.getMsg());
    }

    public String getLocalizedMessage() {
        return String.format("code:%s,msg:%s", this.getCode(), this.getMsg());
    }

    public Result getResult() {
        int currentCode = this.getCode();
        Result result = Result.getByCode(currentCode);
        return result == null ? Result.UNKNOWN : result;
    }
}
