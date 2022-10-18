package com.yomahub.liteflow.parser.dsl.ext

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.components.ext.LogNodeAround
import com.yomahub.liteflow.components.ext.StringFormatNodeAround
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.condition.ChainProxy
import com.yomahub.liteflow.flow.element.condition.NodeCondition
import groovy.transform.TypeChecked
import org.slf4j.event.Level

@TypeChecked
class GYFDslExtension {

    static ChainProxy id(Chain self, String id) {
        return new ChainProxy(self).setId(id)
    }

    static Map<String, Object> add(Map<String, Object> self, String key, Object value) {
        self.put(key, value)
        return self
    }

    static NodeCondition log(NodeCondition self, Level level, String message, Object... args) {
        if (level == null) {
            throw new LiteFlowParseException("log func level must be not null")
        }
        if (StrUtil.isBlank(message)) {
            throw new LiteFlowParseException("log func message must be not null or empty")
        }
        self.addNodeAroundCondition(new LogNodeAround(message, args, level))
        return self
    }

    static NodeCondition strFmt(NodeCondition self, String parameterName, String message, Object... args) {
        if (StrUtil.isBlank(message)) {
            throw new LiteFlowParseException("log func message must be not null or empty")
        }
        self.addNodeAroundCondition(new StringFormatNodeAround(message, args, parameterName))
        return self
    }
}
