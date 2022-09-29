package com.yomahub.liteflow.parser.dsl.ext

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.components.ext.LogNodeAround
import com.yomahub.liteflow.components.ext.StringFormatNodeAround
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.Executable
import com.yomahub.liteflow.flow.element.condition.*
import groovy.transform.TypeChecked
import org.slf4j.event.Level

@TypeChecked
class GYFDslExtension {
    static id(Condition self, String id) {
        self.setId(id)
        return self
    }

    static ignoreError(Condition self, boolean val) {
        self.setErrorResume(val)
        return self
    }

    static id(Chain self, String id) {
        return new ChainProxy(self).setId(id)
    }

    static id(ChainProxy self, String id) {
        return self.setId(id)
    }

    static to(SwitchCondition self, Executable... executables) {
        for (Executable arg : executables) {
            if (arg == null) {
                throw new LiteFlowParseException("The parameter must be Executable item!");
            }
            self.addTargetItem((Executable) arg)
        }
        return self
    }

    static add(Map<String, Object> self, String key, Object value) {
        self.put(key, value)
        return self
    }

    static property(NodeCondition self, String key, Object value) {
        self.addProperty(key, value)
        return self
    }

    static swap(NodeCondition self, String key, Object value) {
        if (StrUtil.isBlank(key)) {
            throw new LiteFlowParseException("swap func key must be not null or empty")
        }
        self.addSwapHandler(key, value)
        return self
    }

    static log(NodeCondition self, Level level, String message, Object... args) {
        if (level == null) {
            throw new LiteFlowParseException("log func level must be not null")
        }
        if (StrUtil.isBlank(message)) {
            throw new LiteFlowParseException("log func message must be not null or empty")
        }
        self.addNodeAroundCondition(new LogNodeAround(message, args, level))
        return self
    }

    static strFmt(NodeCondition self, String parameterName, String message, Object... args) {
        if (StrUtil.isBlank(message)) {
            throw new LiteFlowParseException("log func message must be not null or empty")
        }
        self.addNodeAroundCondition(new StringFormatNodeAround(message, args, parameterName))
        return self
    }

    static threadPool(WhenCondition self, String poolName) {
        if (StrUtil.isBlank(poolName)) {
            throw new LiteFlowParseException("threadPool func poolName must be not null or empty")
        }
        self.setThreadExecutorName(poolName)
        return self
    }
}
