package com.yomahub.liteflow.example.ext;

import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.components.ConstantValueHandler;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.example.exception.BusinessException;
import com.yomahub.liteflow.example.result.ErrorCode;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class BusinessExceptionValueHandler implements ValueHandler {
    private final ErrorCode errorCode;
    private final ValueHandler messageKeyValueHandler;

    public BusinessExceptionValueHandler(ErrorCode errorCode) {
        this(errorCode, (ValueHandler) null);
    }

    public BusinessExceptionValueHandler(ErrorCode errorCode,
                                         ValueHandler messageKeyValueHandler) {
        this.errorCode = errorCode;
        this.messageKeyValueHandler = messageKeyValueHandler;
    }

    public BusinessExceptionValueHandler(ErrorCode errorCode,
                                         String messageKey) {
        this.errorCode = errorCode;
        this.messageKeyValueHandler = new ConstantValueHandler(messageKey);
    }

    @Override
    public Object getValue(Slot slot, Node node) {
        Object messageKey = messageKeyValueHandler == null ? "" : messageKeyValueHandler.getValue(slot, node);
        return new BusinessException(errorCode, ObjectUtil.toString(messageKey));
    }
}
