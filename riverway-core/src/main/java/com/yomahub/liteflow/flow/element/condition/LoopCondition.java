package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.text.CharSequenceUtil;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.components.NodeBreakComponent;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.exception.ComponentResultInvalidException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Node;

public abstract class LoopCondition<T extends LoopCondition<T>> extends Condition<T> {

    protected boolean getBooleanResult(NodeCondition whileNode, Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        Object result = whileNode.execute(slotIndex, flowConfiguration);
        if (!(result instanceof Boolean)) {
            throw new ComponentResultInvalidException(CharSequenceUtil.format("The while component {} result is not a boolean value:{}", whileNode.getNode().getClazz(), result));
        }
        return (Boolean) result;
    }

    protected int getIntegerResult(NodeCondition whileNode, Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        Object result = whileNode.execute(slotIndex, flowConfiguration);
        if (!(result instanceof Integer)) {
            throw new ComponentResultInvalidException(CharSequenceUtil.format("The while component {} result is not a int value:{}", whileNode.getNode().getClazz(), result));
        }
        return (Integer) result;
    }

    protected NodeCondition breakNode;

    public NodeCondition getBreakNode() {
        return breakNode;
    }

    public T setBreakNode(NodeCondition breakNode) {
        this.breakNode = breakNode;
        return getSelf();
    }

    public T DO(Node node) {
        NodeCondition condition = new NodeCondition(node);
        return addExecutable(condition);
    }

    public T DO(NodeCondition node) {
        return addExecutable(node);
    }

    public T BREAK(Node node) {
        NodeComponent component = node.getInstance();
        if (!(component instanceof NodeBreakComponent)) {
            throw new LiteFlowParseException(CharSequenceUtil.format("the component {} is not NodeBreakComponent instance", component.getClass().getName()));
        }
        NodeCondition condition = new NodeCondition(node);
        return setBreakNode(condition);
    }

    public T BREAK(NodeCondition node) {
        NodeComponent component = node.getNode().getInstance();
        if (!(component instanceof NodeBreakComponent)) {
            throw new LiteFlowParseException(CharSequenceUtil.format("the component {} is not NodeBreakComponent instance", component.getClass().getName()));
        }
        return setBreakNode(node);
    }
}
