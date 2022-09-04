package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAround;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAroundCondition;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
        executeBefore(node, slot, slotIndex);
        processSwap(node, slot);
        try {
            node.setCurrChainName(this.getCurrChainName());
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

    protected void executeBefore(Node node, Slot slot, Integer slotIndex) throws Throwable {
        List<Executable> list = this.getExecutableList();
        if (list.isEmpty()) {
            return;
        }
        for (Executable executable : list) {
            if (executable instanceof NodeAroundCondition) {
                executable.execute(slotIndex);
            }
        }
    }

    protected void executeAfter(Node node, Slot slot, Integer slotIndex) throws Throwable {
        List<Executable> list = this.getExecutableList();
        if (list.isEmpty()) {
            return;
        }
        for (Executable executable : list) {
            if (executable instanceof NodeAroundCondition) {
                ((NodeAroundCondition) executable).executeAfter(slotIndex);
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

    public void addNodeAroundCondition(NodeAroundCondition nodeAroundCondition) {
        this.getExecutableList().add(nodeAroundCondition);
    }

    public void addNodeAroundCondition(NodeAround nodeAround) {
        this.getExecutableList().add(new NodeAroundCondition(this, nodeAround));
    }
}
