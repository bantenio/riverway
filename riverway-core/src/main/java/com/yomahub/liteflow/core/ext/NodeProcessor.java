package com.yomahub.liteflow.core.ext;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public interface NodeProcessor {
    default Object handle(Node node, Slot slot) throws Throwable {
        if (hasResult()) {
            return processWithResult(node, slot);
        } else {
            process(node, slot);
            return null;
        }
    }

    void process(Node node, Slot slot) throws Throwable;

    Object processWithResult(Node node, Slot slot) throws Throwable;

    Slot getSlot();

    default boolean hasResult() {
        return false;
    }
}
