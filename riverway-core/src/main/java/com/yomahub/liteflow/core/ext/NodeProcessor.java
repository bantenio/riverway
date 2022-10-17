package com.yomahub.liteflow.core.ext;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

import java.util.Optional;

public interface NodeProcessor {
    default Object process(Node node) throws Throwable {
        if (hasResult()) {
            return processWithResult(node, getSlot());
        } else {
            process(node, getSlot());
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
