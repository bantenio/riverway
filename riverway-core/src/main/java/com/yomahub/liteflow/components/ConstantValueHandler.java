package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class ConstantValueHandler implements ValueHandler {
    public final Object value;

    public ConstantValueHandler(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue(Slot slot, Node node) {
        return value;
    }
}
