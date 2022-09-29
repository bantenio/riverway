package com.yomahub.liteflow.example.ext.flow.gyf;

import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.example.ext.BusinessExceptionValueHandler;
import com.yomahub.liteflow.example.result.ErrorCode;

public class ExtFunc {
    public static ValueHandler w_BizErr(ErrorCode errorCode, String messageKey) {
        return new BusinessExceptionValueHandler(errorCode, messageKey);
    }

    public static ValueHandler w_BizErr(ErrorCode errorCode) {
        return new BusinessExceptionValueHandler(errorCode);
    }
}
