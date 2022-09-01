package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

import java.util.HashMap;
import java.util.Map;

public class NodeCondition extends Condition {
    private final Node node;

    private final Map<String, Object> properties = new HashMap<>();

    private final Map<String, Object> swaps = new HashMap<>();

    public NodeCondition(Node node) {
        this.node = node;
    }

    @Override
    public void execute(Integer slotIndex) throws Throwable {
        Slot slot = DataBus.getSlot(slotIndex);
        slot.putProperties(properties);
        processSwap(node, slot);
        try {
            node.execute(slotIndex);
        } finally {
            slot.clearProperties();
        }
    }

    public void processSwap(Node node, Slot slot) throws Throwable {
        if (swaps.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : swaps.entrySet()) {
            Object valRef = entry.getValue();
            if (valRef instanceof String) {
                slot._putInParameter(entry.getKey(), slot.getVariable(valRef.toString()));
            } else if (valRef instanceof ValueHandler) {
                Object val = ((ValueHandler) valRef).getValue(slot, node);
                slot._putInParameter(entry.getKey(), val);
            }
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_NODE;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public void addSwapHandler(String key, Object valSwap) {
        swaps.put(key, valSwap);
    }

    public Node getNode() {
        return this.node;
    }

    @Override
    public String getExecuteName() {
        return StrUtil.blankToDefault(this.getId(), node.getExecuteName());
    }
}
