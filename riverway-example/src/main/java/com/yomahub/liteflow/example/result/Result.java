package com.yomahub.liteflow.example.result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Result {
    private static final Map<Integer, Result> REGISTER_RESULT = new ConcurrentHashMap();
    public static final Result UNKNOWN = new Result(-1, "response.failure.sysfail");
    private final int code;
    private final String message;

    public Result(int code, String msg) {
        this.code = code;
        this.message = msg;
        REGISTER_RESULT.put(code, this);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "ResultCode{code=" + this.code + ", message='" + this.message + "'}";
    }

    public static Result getByCode(int code) {
        return REGISTER_RESULT.getOrDefault(code, null);
    }
}
