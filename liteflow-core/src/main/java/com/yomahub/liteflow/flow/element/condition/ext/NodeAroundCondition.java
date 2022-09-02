package com.yomahub.liteflow.flow.element.condition.ext;

import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.slot.DataBus;

public class NodeAroundCondition implements Executable {
    private final NodeCondition nodeCondition;

    private final NodeAround nodeAround;

    public NodeAroundCondition(NodeCondition nodeCondition, NodeAround nodeAround) {
        this.nodeCondition = nodeCondition;
        this.nodeAround = nodeAround;
    }

    @Override
    public void execute(Integer slotIndex) throws Throwable {
        nodeAround.before(nodeCondition, nodeCondition.getNode(), DataBus.getSlot(slotIndex));
    }

    public void executeAfter(Integer slotIndex) throws Throwable {
        nodeAround.after(nodeCondition, nodeCondition.getNode(), DataBus.getSlot(slotIndex));
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
