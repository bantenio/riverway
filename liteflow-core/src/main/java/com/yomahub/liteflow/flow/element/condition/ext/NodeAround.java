package com.yomahub.liteflow.flow.element.condition.ext;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.slot.Slot;

public interface NodeAround {

    default void before(NodeCondition nodeCondition, Node node, Slot slot) throws Throwable {

    }

    default void after(NodeCondition nodeCondition, Node node, Slot slot) throws Throwable {

    }
}
