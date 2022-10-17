package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class MutableValueHandler implements ValueHandler {
    public Object value;

    public MutableValueHandler(Object value) {
        this.value = value;
    }

    public MutableValueHandler() {}

    public MutableValueHandler setValue(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public Object getValue(Slot slot, Node node) {
        return value;
    }
}
