package com.yomahub.liteflow.example.ext.flow.gyf;

import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.components.ConstantValueHandler;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.example.exception.BaseBusinessException;
import com.yomahub.liteflow.example.result.Result;
import org.codehaus.groovy.runtime.MethodClosure;

public class BusinessExceptionClosure extends MethodClosure {
    public BusinessExceptionClosure(Class<?> clazz, String methodName) {
        super(clazz, methodName);
    }

    @Override
    public ValueHandler call(Object... args) {
        Result errorCode = Result.UNKNOWN;
        ValueHandler errorCodeValueHandler = null;
        String messageKey = errorCode.getMessage();
        if (args.length > 0) {
            Object var = args[0];
            if (!(var instanceof Result) && !(var instanceof ValueHandler)) {
                throw new LiteFlowParseException("parameter 0 must be Result or ValueHandler");
            }
            if (var instanceof Result) {
                errorCode = (Result) var;
            } else {
                errorCodeValueHandler = (ValueHandler) var;
            }
        }
        if (args.length > 1) {
            Object var = args[1];
            if (!(var instanceof String)) {
                throw new LiteFlowParseException("parameter 1 must be String");
            }
            messageKey = (String) var;
        }
        errorCodeValueHandler = errorCodeValueHandler != null ? errorCodeValueHandler : new ConstantValueHandler(new BaseBusinessException(errorCode, messageKey));
        return errorCodeValueHandler;
    }
}
