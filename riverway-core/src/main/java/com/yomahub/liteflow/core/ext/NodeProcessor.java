package com.yomahub.liteflow.core.ext;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public interface NodeProcessor {
    default void process(Node node) throws Throwable {
        process(node, getSlot());
    }

    void process(Node node, Slot slot) throws Throwable;

    Slot getSlot();
}
