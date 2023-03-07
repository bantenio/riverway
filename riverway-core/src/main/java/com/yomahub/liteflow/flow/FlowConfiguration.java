package com.yomahub.liteflow.flow;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.executor.DefaultNodeExecutor;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import com.yomahub.liteflow.flow.id.DefaultRequestIdGenerator;
import com.yomahub.liteflow.flow.id.RequestIdGenerator;
import com.yomahub.liteflow.parser.ResourceParser;
import com.yomahub.liteflow.parser.ext.HuToolResourceParser;
import com.yomahub.liteflow.plugins.PluginManager;
import com.yomahub.liteflow.property.LiteFlowConfig;
import com.yomahub.liteflow.thread.ExecutorServiceManager;

import java.util.Map;

public class FlowConfiguration {

    private ChainManager chainManager = new ChainManager();

    private NodeManager nodeManager = new NodeManager();

    private NodeComponentManager nodeComponentManager = new NodeComponentManager();

    private ExecutorServiceManager executorServiceManager;

    private LiteFlowConfig liteflowConfig;

    private RequestIdGenerator requestIdGenerator = new DefaultRequestIdGenerator();

    private FlowExecutor flowExecutor;

    private NodeExecutor nodeExecutor = new DefaultNodeExecutor();

    private PluginManager pluginManager = new PluginManager();

    private ResourceParser resourceParser = new HuToolResourceParser();

    public FlowConfiguration(LiteFlowConfig liteflowConfig) {
        this.liteflowConfig = liteflowConfig;
        executorServiceManager = new ExecutorServiceManager();
        flowExecutor = new FlowExecutor(liteflowConfig, this);
    }

    public ChainManager getChainManager() {
        return chainManager;
    }

    public FlowConfiguration setChainManager(ChainManager chainManager) {
        this.chainManager = chainManager;
        return this;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public FlowConfiguration setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
        return this;
    }

    public NodeComponentManager getNodeComponentManager() {
        return nodeComponentManager;
    }

    public FlowConfiguration setNodeComponentManager(NodeComponentManager nodeComponentManager) {
        this.nodeComponentManager = nodeComponentManager;
        return this;
    }

    public Chain getChain(String id) {
        return chainManager.getChain(id);
    }

    public void addChain(String chainName) {
        chainManager.addChain(chainName);
    }

    public void addChain(Chain chain) {
        chainManager.addChain(chain);
    }

    public boolean containChain(String chainId) {
        return chainManager.containChain(chainId);
    }

    public boolean removeChain(String chainId) {
        return chainManager.removeChain(chainId);
    }

    public Node getNode(String nodeId) {
        return nodeManager.getNode(nodeId);
    }

    public Node copyNode(String nodeId) {
        return nodeManager.copyNode(nodeId);
    }

    public boolean containNode(String nodeId) {
        return nodeManager.containNode(nodeId);
    }

    public Node addNode(String nodeId, String name, NodeComponent nodeComponent) {
        return nodeManager.addNode(nodeId, name, nodeComponent);
    }

    public NodeComponent getNodeComponent(String nodeId) {
        return nodeComponentManager.getNodeComponent(nodeId);
    }

    public boolean hasNodeComponent(String nodeId) {
        return nodeComponentManager.hasNodeComponent(nodeId);
    }

    public void addNodeComponent(String nodeId, NodeComponent nodeComponent) {
        nodeComponentManager.addNodeComponent(nodeId, nodeComponent);
    }

    public Map<String, Chain> getChainMap() {
        return chainManager.getChainMap();
    }

    public Map<String, Node> getNodeMap() {
        return nodeManager.getNodeMap();
    }

    public ExecutorServiceManager getExecutorServiceManager() {
        return executorServiceManager;
    }

    public FlowConfiguration setExecutorServiceManager(ExecutorServiceManager executorServiceManager) {
        this.executorServiceManager = executorServiceManager;
        return this;
    }

    public LiteFlowConfig getLiteflowConfig() {
        return liteflowConfig;
    }

    public FlowConfiguration setLiteflowConfig(LiteFlowConfig liteflowConfig) {
        this.liteflowConfig = liteflowConfig;
        return this;
    }

    public RequestIdGenerator getRequestIdGenerator() {
        return requestIdGenerator;
    }

    public FlowConfiguration setRequestIdGenerator(RequestIdGenerator requestIdGenerator) {
        this.requestIdGenerator = requestIdGenerator;
        return this;
    }

    public FlowExecutor getFlowExecutor() {
        return flowExecutor;
    }

    public FlowConfiguration setFlowExecutor(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
        return this;
    }

    public NodeExecutor getNodeExecutor() {
        return nodeExecutor;
    }

    public FlowConfiguration setNodeExecutor(NodeExecutor nodeExecutor) {
        this.nodeExecutor = nodeExecutor;
        return this;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public FlowConfiguration setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        return this;
    }

    public ResourceParser getResourceParser() {
        return resourceParser;
    }

    public FlowConfiguration setResourceParser(ResourceParser resourceParser) {
        this.resourceParser = resourceParser;
        return this;
    }
}
