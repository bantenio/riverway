package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class ThrowComponent extends NodeComponent {

    private final Throwable exception;

    public ThrowComponent(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public void process(Node node, Slot slot) throws Throwable {
        throw exception;
    }
}
