package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.text.CharSequenceUtil;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAround;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAroundCondition;
import com.yomahub.liteflow.slot.Slot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeCondition extends Condition<NodeCondition> {
    private final Node node;

    private final Map<String, Object> properties = new HashMap<>();

    private final Map<String, Object> swaps = new HashMap<>();

    public NodeCondition(Node node) {
        this.node = node;
    }

    @Override
    public Object execute(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        slot.putProperties(properties);
        executeBefore(node, slot, flowConfiguration);
        processSwap(node, slot);
        Object result = null;
        try {
            node.setCurrChainName(this.getCurrChainName());
            result = node.execute(slot, flowConfiguration);
        } finally {
            slot.clearProperties();
        }
        return result;
    }

    @Override
    public void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {

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
            } else {
                slot._putInParameter(entry.getKey(), valRef);
            }
        }
    }

    protected void executeBefore(Node node, Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        List<Executable<? extends Executable<?>>> list = this.getExecutableList();
        if (list.isEmpty()) {
            return;
        }
        for (Executable<? extends Executable<?>> executable : list) {
            if (executable instanceof NodeAroundCondition) {
                executable.execute(slot, flowConfiguration);
            }
        }
    }

    protected void executeAfter(Node node, Slot slot) throws Throwable {
        List<Executable<? extends Executable<?>>> list = this.getExecutableList();
        if (list.isEmpty()) {
            return;
        }
        for (Executable<? extends Executable<?>> executable : list) {
            if (executable instanceof NodeAroundCondition) {
                ((NodeAroundCondition) executable).executeAfter(slot);
            }
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_NODE;
    }

    public NodeCondition addProperty(String key, Object value) {
        properties.put(key, value);
        return getSelf();
    }

    public NodeCondition property(String key, Object value) {
        properties.put(key, value);
        return getSelf();
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public <T> T getPropertyByType(String key) {
        return (T) properties.get(key);
    }


    public <T> T property(String key) {
        return getPropertyByType(key);
    }

    public NodeCondition addSwapHandler(String key, Object valSwap) {
        swaps.put(key, valSwap);
        return getSelf();
    }

    public NodeCondition swap(String key, Object valSwap) {
        swaps.put(key, valSwap);
        return getSelf();
    }

    public Node getNode() {
        return this.node;
    }

    @Override
    public String getExecuteName() {
        return CharSequenceUtil.blankToDefault(this.getId(), node.getExecuteName());
    }

    public NodeCondition addNodeAroundCondition(NodeAroundCondition nodeAroundCondition) {
        this.getExecutableList().add(nodeAroundCondition);
        return getSelf();
    }

    public NodeCondition addNodeAroundCondition(NodeAround nodeAround) {
        this.getExecutableList().add(new NodeAroundCondition(this, nodeAround));
        return getSelf();
    }

    public NodeCondition nodeAround(NodeAroundCondition nodeAroundCondition) {
        this.getExecutableList().add(nodeAroundCondition);
        return getSelf();
    }

    public NodeCondition nodeAround(NodeAround nodeAround) {
        this.getExecutableList().add(new NodeAroundCondition(this, nodeAround));
        return getSelf();
    }


    @Override
    public boolean hasResult() {
        return node.hasResult();
    }
}
