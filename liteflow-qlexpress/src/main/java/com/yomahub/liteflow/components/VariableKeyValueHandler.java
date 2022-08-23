package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class VariableKeyValueHandler implements ValueHandler {
    private String variableKey;

    public VariableKeyValueHandler(String variableKey) {
        this.variableKey = variableKey;
    }

    @Override
    public Object getValue(Slot slot, Node node) {
        return slot.getVariable(variableKey);
    }
}
