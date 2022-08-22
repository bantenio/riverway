package com.yomahub.liteflow.flow.element.condition;

import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

import java.util.HashMap;
import java.util.Map;

public class NodeCondition extends Condition {
    private final Node node;

    private final Map<String, Object> properties = new HashMap<>();

    public NodeCondition(Node node) {
        this.node = node;
    }

    @Override
    public void execute(Integer slotIndex) throws Exception {
        Slot slot = DataBus.getSlot(slotIndex);
        slot.putProperties(properties);
        try {
            node.execute(slotIndex);
        } finally {
            slot.clearProperties();
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_NODE;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }
}
