package com.yomahub.liteflow.plugins;

import com.yomahub.liteflow.flow.element.Node;

public class NodeComponentInterceptorContext extends InterceptorContext {
    private Node node;

    public Node getNode() {
        return node;
    }

    public NodeComponentInterceptorContext setNode(Node node) {
        this.node = node;
        return this;
    }

    @Override
    public String toString() {
        return "NodeComponentInterceptorContext{" +
                "node=" + node +
                '}';
    }
}
