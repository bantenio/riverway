package com.yomahub.liteflow.example.exception;

import com.yomahub.liteflow.example.result.ErrorCode;
import com.yomahub.liteflow.example.result.Result;

public class BusinessException extends BaseBusinessException {
    public BusinessException() {
        this(ErrorCode.UNKNOWN);
    }

    public BusinessException(ErrorCode errorCode) {
        super(new Result(errorCode.getCode(),errorCode.getMsg()));
    }

    public BusinessException(ErrorCode errorCode, String customMsg) {
        super(new Result(errorCode.getCode(),customMsg));
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(new Result(errorCode.getCode(),errorCode.getMsg()),cause);
    }

    public BusinessException(ErrorCode errorCode, String customMsg, Throwable cause) {
        super(new Result(errorCode.getCode(),customMsg),cause);
    }

    public BusinessException(Integer code, String customMsg){
        super(new Result(code,customMsg));
    }

    public BusinessException(Integer code, String customMsg, Throwable cause){
        super(new Result(code,customMsg),cause);
    }

    @Override
    public String toString() {
        return String.format("BusinessException code:%s,msg:%s",getCode(),getMsg());
    }

    @Override
    public String getMessage() {
        return String.format("code:%s,msg:%s",getCode(),getMsg());
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("code:%s,msg:%s",getCode(),getMsg());
    }

    public ErrorCode getErrorCode(){
        for(ErrorCode code:ErrorCode.values()){
            if(this.getCode()==code.getCode()){
                return code;
            }
        }
        return ErrorCode.UNKNOWN;
    }
}
