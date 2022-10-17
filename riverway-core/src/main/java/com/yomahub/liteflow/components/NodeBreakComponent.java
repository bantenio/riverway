package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public abstract class NodeBreakComponent extends NodeComponent {

    @Override
    public Object processWithResult(Node node, Slot slot) throws Throwable {
        return this.processBreak(node, slot);
    }

    public abstract boolean processBreak(Node node, Slot slot) throws Exception;

    @Override
    public boolean hasResult() {
        return true;
    }
}
