package com.yomahub.liteflow.core;

import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.annotation.LiteflowRetry;
import com.yomahub.liteflow.annotation.util.AnnoUtil;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import com.yomahub.liteflow.property.LiteflowConfig;

/**
 * 组件初始化器
 * @author Bryan.Zhang
 * @since 2.6.0
 */
public class ComponentInitializer {

    private static ComponentInitializer instance;

    public static ComponentInitializer loadInstance(){
        if (ObjectUtil.isNull(instance)){
            instance = new ComponentInitializer();
        }
        return instance;
    }

    public NodeComponent initComponent(Node node,
                                       NodeComponent nodeComponent,
                                       NodeTypeEnum type,
                                       String desc,
                                       String nodeId,
                                       LiteflowConfig liteflowConfig) {
        nodeComponent.setNodeId(nodeId);
        nodeComponent.setSelf(nodeComponent);
        nodeComponent.setType(type);

        //先取传进来的name值(配置文件中配置的)，再看有没有配置@LiteflowComponent标注
        //@LiteflowComponent标注只在spring体系下生效，这里用了spi机制取到相应环境下的实现类
        nodeComponent.setName(desc);

        //先从组件上取@RetryCount标注，如果没有，则看全局配置，全局配置如果不配置的话，默认是0
        //默认retryForExceptions为Exception.class
        LiteflowRetry liteflowRetryAnnotation = AnnoUtil.getAnnotation(nodeComponent.getClass(), LiteflowRetry.class);
        if (ObjectUtil.isNotNull(liteflowRetryAnnotation)) {
            node.setRetryCount(liteflowRetryAnnotation.retry());
            nodeComponent.setRetryForExceptions(liteflowRetryAnnotation.forExceptions());
        } else {
            node.setRetryCount(liteflowConfig.getRetryCount());
        }
        nodeComponent.setNodeExecutorClass(buildNodeExecutorClass(liteflowConfig));

        return nodeComponent;
    }

    private Class<? extends NodeExecutor> buildNodeExecutorClass(LiteflowConfig liteflowConfig) {
        Class<?> nodeExecutorClass;
        try {
            nodeExecutorClass = Class.forName(liteflowConfig.getNodeExecutorClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return (Class<? extends NodeExecutor>) nodeExecutorClass;
    }
}
