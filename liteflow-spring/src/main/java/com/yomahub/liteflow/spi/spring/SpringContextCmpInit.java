package com.yomahub.liteflow.spi.spring;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.NodeComponentManager;
import com.yomahub.liteflow.spring.ComponentScanner;

import java.util.Map;

/**
 * Spring环境容器上下文组件初始化实现
 * @author Bryan.Zhang
 * @since 2.6.11
 */
public class SpringContextCmpInit {
    public void initCmp(FlowConfiguration flowConfiguration) {
        NodeComponentManager nodeComponentManager = flowConfiguration.getNodeComponentManager();
        for (Map.Entry<String, NodeComponent> componentEntry : ComponentScanner.nodeComponentMap.entrySet()) {
            if (!nodeComponentManager.hasNodeComponent(componentEntry.getKey())) {
                nodeComponentManager.addNodeComponent(componentEntry.getKey(), componentEntry.getValue());
            }
        }
    }

    public int priority() {
        return 1;
    }
}
