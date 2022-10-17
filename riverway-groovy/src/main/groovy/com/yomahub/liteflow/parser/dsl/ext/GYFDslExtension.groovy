package com.yomahub.liteflow.parser.dsl.ext

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.components.NodeBreakComponent
import com.yomahub.liteflow.components.ext.LogNodeAround
import com.yomahub.liteflow.components.ext.StringFormatNodeAround
import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.Executable
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.*
import groovy.transform.TypeChecked
import org.slf4j.event.Level

@TypeChecked
class GYFDslExtension {
    static Condition id(Condition self, String id) {
        self.setId(id)
        return self
    }

    static Condition ignoreError(Condition self, boolean val) {
        self.setErrorResume(val)
        return self
    }

    static ChainProxy id(Chain self, String id) {
        return new ChainProxy(self).setId(id)
    }

    static ChainProxy id(ChainProxy self, String id) {
        return self.setId(id)
    }

    static SwitchCondition to(SwitchCondition self, Executable... executables) {
        for (Executable arg : executables) {
            if (arg == null) {
                throw new LiteFlowParseException("The parameter must be Executable item!");
            }
            self.addTargetItem((Executable) arg)
        }
        return self
    }

    static Map<String, Object> add(Map<String, Object> self, String key, Object value) {
        self.put(key, value)
        return self
    }

    static NodeCondition property(NodeCondition self, String key, Object value) {
        self.addProperty(key, value)
        return self
    }

    static NodeCondition swap(NodeCondition self, String key, Object value) {
        if (StrUtil.isBlank(key)) {
            throw new LiteFlowParseException("swap func key must be not null or empty")
        }
        self.addSwapHandler(key, value)
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

    static WhenCondition threadPool(WhenCondition self, String poolName) {
        if (StrUtil.isBlank(poolName)) {
            throw new LiteFlowParseException("threadPool func poolName must be not null or empty")
        }
        self.setThreadExecutorName(poolName)
        return self
    }

    static LoopCondition DO(LoopCondition self, Node node) {
        NodeCondition condition = new NodeCondition(node)
        self.addExecutable(condition)
        return self
    }

    static LoopCondition DO(LoopCondition self, NodeCondition node) {
        self.addExecutable(node)
        return self
    }

    static LoopCondition BREAK(LoopCondition self, Node node) {
        NodeComponent component = node.getInstance()
        if (!(component instanceof NodeBreakComponent)) {
            throw new LiteFlowParseException(StrUtil.format("the component {} is not NodeBreakComponent instance", component.getClass().getName()))
        }
        NodeCondition condition = new NodeCondition(node)
        self.setBreakNode(condition)
        return self
    }

    static LoopCondition BREAK(LoopCondition self, NodeCondition node) {
        NodeComponent component = node.getNode().getInstance()
        if (!(component instanceof NodeBreakComponent)) {
            throw new LiteFlowParseException(StrUtil.format("the component {} is not NodeBreakComponent instance", component.getClass().getName()))
        }
        self.setBreakNode(node)
        return self
    }
}
