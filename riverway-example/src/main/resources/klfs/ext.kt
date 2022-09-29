package klfs

import com.yomahub.liteflow.components.ValueHandler
import com.yomahub.liteflow.example.ext.BusinessExceptionValueHandler
import com.yomahub.liteflow.example.result.ErrorCode

fun w_BizErr(errorCode: ErrorCode, messageKey: String?): ValueHandler {
    return BusinessExceptionValueHandler(errorCode, messageKey)
}

fun w_BizErr(errorCode: ErrorCode): ValueHandler {
    return BusinessExceptionValueHandler(errorCode)
}

fun w_BizErr(valueHandler: ValueHandler): ValueHandler {
    return valueHandler
}
