package com.yomahub.liteflow.builder;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.ChainManager;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.NodeComponentManager;
import com.yomahub.liteflow.flow.NodeManager;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import com.yomahub.liteflow.flow.id.RequestIdGenerator;
import com.yomahub.liteflow.plugins.PluginManager;
import com.yomahub.liteflow.property.LiteFlowConfig;
import com.yomahub.liteflow.thread.ExecutorServiceManager;

import java.util.List;
import java.util.Map;

public class FlowConfigurationBuilder {

    private LiteFlowConfig liteflowConfig;

    private RequestIdGenerator requestIdGenerator;

    private NodeExecutor nodeExecutor;

    private ChainManager chainManager;

    private NodeManager nodeManager;

    private NodeComponentManager nodeComponentManager = new NodeComponentManager();

    private ExecutorServiceManager executorServiceManager;

    private Map<String, NodeComponent> nodeComponentMap;

    private PluginManager pluginManager;

    public static FlowConfigurationBuilder create() {
        return new FlowConfigurationBuilder();
    }

    public FlowConfigurationBuilder setLiteFlowConfig(LiteFlowConfig liteflowConfig) {
        this.liteflowConfig = liteflowConfig;
        return this;
    }

    public FlowConfigurationBuilder setRequestIdGenerator(RequestIdGenerator requestIdGenerator) {
        this.requestIdGenerator = requestIdGenerator;
        return this;
    }

    public FlowConfigurationBuilder setNodeExecutor(NodeExecutor nodeExecutor) {
        this.nodeExecutor = nodeExecutor;
        return this;
    }

    public FlowConfigurationBuilder setChainManager(ChainManager chainManager) {
        this.chainManager = chainManager;
        return this;
    }

    public FlowConfigurationBuilder setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
        return this;
    }

    public FlowConfigurationBuilder setNodeComponentManager(NodeComponentManager nodeComponentManager) {
        this.nodeComponentManager = nodeComponentManager;
        return this;
    }

    public ExecutorServiceManager getExecutorServiceManager() {
        return executorServiceManager;
    }

    public FlowConfigurationBuilder setExecutorServiceManager(ExecutorServiceManager executorServiceManager) {
        this.executorServiceManager = executorServiceManager;
        return this;
    }

    public FlowConfigurationBuilder setNodeComponentMap(Map<String, NodeComponent> nodeComponentMap) {
        this.nodeComponentMap = nodeComponentMap;
        return this;
    }

    public FlowConfigurationBuilder setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        return this;
    }

    public FlowConfiguration build() {
        FlowConfiguration flowConfiguration = new FlowConfiguration(liteflowConfig);
        if (nodeExecutor != null) {
            flowConfiguration.setNodeExecutor(nodeExecutor);
        }
        if (requestIdGenerator != null) {
            flowConfiguration.setRequestIdGenerator(requestIdGenerator);
        }
        if (chainManager != null) {
            flowConfiguration.setChainManager(chainManager);
        }
        if (nodeManager != null) {
            flowConfiguration.setNodeManager(nodeManager);
        }
        if (nodeComponentManager != null) {
            flowConfiguration.setNodeComponentManager(nodeComponentManager);
        }
        if(nodeComponentMap != null) {
            flowConfiguration.getNodeComponentManager().addAllNodeComponent(nodeComponentMap);
        }
        if (executorServiceManager != null) {
            flowConfiguration.setExecutorServiceManager(executorServiceManager);
        }
        if (pluginManager != null) {
            flowConfiguration.setPluginManager(pluginManager);
        }
        List<String> paths = liteflowConfig.getFlowPaths();
        if (paths != null) {
            for (String path : paths) {
                FlowParserProvider.parse(path, flowConfiguration);
            }
        }
        return flowConfiguration;
    }
}
