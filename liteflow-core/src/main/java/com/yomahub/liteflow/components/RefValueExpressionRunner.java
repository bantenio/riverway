package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

public interface RefValueExpressionRunner {
    Object getValue(Slot slot, Node node, String expression) throws RuntimeException;
}
