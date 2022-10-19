package com.yomahub.liteflow.flow.element.condition.ext;

import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

public class NodeAroundCondition implements Executable<NodeAroundCondition> {
    private final NodeCondition nodeCondition;

    private final NodeAround nodeAround;

    public NodeAroundCondition(NodeCondition nodeCondition, NodeAround nodeAround) {
        this.nodeCondition = nodeCondition;
        this.nodeAround = nodeAround;
    }

    @Override
    public NodeAroundCondition getSelf() {
        return this;
    }

    @Override
    public void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        nodeAround.before(nodeCondition, nodeCondition.getNode(), slot);
    }

    public void executeAfter(Slot slot) throws Throwable {
        nodeAround.after(nodeCondition, nodeCondition.getNode(), slot);
    }

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return ExecuteTypeEnum.AROUND;
    }

    @Override
    public String getExecuteName() {
        return null;
    }
}
