package com.yomahub.liteflow.flow.element.condition;

import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

import java.util.HashMap;
import java.util.Map;

public class NodeCondition extends Condition {
    private final Node node;

    private final Map<String, Object> params = new HashMap<>();

    public NodeCondition(Node node) {
        this.node = node;
    }

    @Override
    public void execute(Integer slotIndex) throws Exception {
        Slot slot = DataBus.getSlot(slotIndex);
        slot.putParameter(params);
        try {
            node.execute(slotIndex);
        } finally {
            slot.clearParameter();
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_NODE;
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }
}
