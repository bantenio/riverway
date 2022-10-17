package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public abstract class NodeWhileComponent extends NodeComponent {

    @Override
    public Object processWithResult(Node node, Slot slot) throws Throwable {
        return this.processWhile(node, slot);
    }

    public abstract boolean processWhile(Node node, Slot slot) throws Exception;

    @Override
    public boolean hasResult() {
        return true;
    }
}
