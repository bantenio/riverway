package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class ThrowComponent extends NodeComponent {

    private final ValueHandler exceptionValueHandler;

    public ThrowComponent(Throwable exception) {
        this.exceptionValueHandler = new ConstantValueHandler(exception);
    }

    public ThrowComponent(ValueHandler exception) {
        this.exceptionValueHandler = exception;

    }

    @Override
    public void process(Node node, Slot slot) throws Throwable {
        throw (Throwable) exceptionValueHandler.getValue(slot, node);
    }
}
