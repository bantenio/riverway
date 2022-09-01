package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.entity.CmpStep;

public class NodeComponentInterceptorContext extends ChainInterceptorContext<NodeComponentInterceptorContext> {


    private Node node;

    private NodeComponent nodeComponent;

    private boolean retry;

    private CmpStep cmpStep;

    public Node getNode() {
        return node;
    }

    public NodeComponentInterceptorContext setNode(Node node) {
        this.node = node;
        return this;
    }

    public NodeComponent getNodeComponent() {
        return nodeComponent;
    }

    public NodeComponentInterceptorContext setNodeComponent(NodeComponent nodeComponent) {
        this.nodeComponent = nodeComponent;
        return this;
    }

    public boolean isRetry() {
        return retry;
    }

    public NodeComponentInterceptorContext setRetry(boolean retry) {
        this.retry = retry;
        return this;
    }

    public CmpStep getCmpStep() {
        return cmpStep;
    }

    public NodeComponentInterceptorContext setCmpStep(CmpStep cmpStep) {
        this.cmpStep = cmpStep;
        return this;
    }

    @Override
    public String toString() {
        return "NodeComponentInterceptorContext{" +
                "node=" + node +
                ", nodeComponent=" + nodeComponent +
                ", retry=" + retry +
                ", cmpStep=" + cmpStep +
                "} " + super.toString();
    }
}
