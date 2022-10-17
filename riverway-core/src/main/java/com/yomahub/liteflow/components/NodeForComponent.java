package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public class NodeForComponent extends NodeComponent {

    private int loopCount = 0;

    public NodeForComponent() {
    }

    public NodeForComponent(int loopCount) {
        this.loopCount = loopCount;
    }

    @Override
    public Object processWithResult(Node node, Slot slot) throws Throwable {
        return this.processFor(node, slot);
    }

    public int processFor(Node node, Slot slot) throws Exception {
        return loopCount;
    }

    @Override
    public boolean hasResult() {
        return true;
    }
}
