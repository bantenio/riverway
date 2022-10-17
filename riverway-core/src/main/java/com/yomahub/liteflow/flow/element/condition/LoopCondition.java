package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.exception.ComponentResultInvalidException;
import com.yomahub.liteflow.flow.FlowConfiguration;

public abstract class LoopCondition extends Condition {

    protected boolean getBooleanResult(NodeCondition whileNode, Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        Object result = whileNode.execute(slotIndex, flowConfiguration);
        if (result == null || !(result instanceof Boolean)) {
            throw new ComponentResultInvalidException(StrUtil.format("The while component {} result is not a boolean value:{}", whileNode.getNode().getClazz(), result));
        }
        return (Boolean) result;
    }

    protected int getIntegerResult(NodeCondition whileNode, Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        Object result = whileNode.execute(slotIndex, flowConfiguration);
        if (result == null || !(result instanceof Integer)) {
            throw new ComponentResultInvalidException(StrUtil.format("The while component {} result is not a int value:{}", whileNode.getNode().getClazz(), result));
        }
        return (Integer) result;
    }
    protected NodeCondition breakNode;

    public NodeCondition getBreakNode() {
        return breakNode;
    }

    public void setBreakNode(NodeCondition breakNode) {
        this.breakNode = breakNode;
    }
}
