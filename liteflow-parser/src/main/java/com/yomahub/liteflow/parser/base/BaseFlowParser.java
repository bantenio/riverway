package com.yomahub.liteflow.parser.base;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowSwitchCmpDefine;
import com.yomahub.liteflow.builder.*;
import com.yomahub.liteflow.builder.prop.ChainPropBean;
import com.yomahub.liteflow.builder.prop.NodePropBean;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.core.proxy.ComponentProxy;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.*;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Node;

public abstract class BaseFlowParser implements FlowParser {


    public Chain buildChain(ChainPropBean chainPropBean,
                            LiteFlowChainBuilder chainBuilder,
                            FlowConfiguration flowConfiguration,
                            ParseResource objectResource) {
        String condValueStr = chainPropBean.getCondValueStr();
        String group = chainPropBean.getGroup();
        String errorResume = chainPropBean.getErrorResume();
        String any = chainPropBean.getAny();
        String threadExecutorName = chainPropBean.getThreadExecutorName();
        ConditionTypeEnum conditionType = chainPropBean.getConditionType();

        if (ObjectUtil.isNull(conditionType)) {
            throw new NotSupportConditionException(StrUtil.format("ConditionType is not supported in {} of {}", chainPropBean.getChainName(), objectResource.getResource()));
        }

        if (StrUtil.isBlank(condValueStr)) {
            throw new EmptyConditionValueException("Condition value cannot be empty");
        }

        Chain chain;
        //如果是when类型的话，有特殊化参数要设置，只针对于when的
        if (conditionType.equals(ConditionTypeEnum.TYPE_WHEN)) {
            chain = chainBuilder.setCondition(
                    LiteFlowConditionBuilder.createWhenCondition(flowConfiguration)
                            .setErrorResume(errorResume)
                            .setGroup(group)
                            .setAny(any)
                            .setThreadExecutorName(threadExecutorName)
                            .setValue(condValueStr)
                            .build()
            ).build();
        } else {
            chain = chainBuilder.setCondition(
                    LiteFlowConditionBuilder.createCondition(conditionType, flowConfiguration)
                            .setValue(condValueStr)
                            .build()
            ).build();
        }
        return chain;
    }

    public Node buildNode(NodePropBean nodePropBean, FlowConfiguration flowConfiguration) {
        String id = nodePropBean.getId();
        String name = nodePropBean.getName();
        String clazz = nodePropBean.getClazz();
        String type = null;

        NodeComponent nodeComponent = flowConfiguration.getNodeComponent(id);
        if (nodeComponent != null) {
            type = nodeComponent.getType().getCode();
        }
        // ! TODO 修改为多种NodeComponent对象创建机制
        //先尝试自动推断类型
        if (nodeComponent == null && StrUtil.isNotBlank(clazz)) {
            try {
                //先尝试从继承的类型中推断
                Class<?> c = Class.forName(clazz);
                if (NodeSwitchComponent.class.isAssignableFrom(c)) {
                    type = NodeTypeEnum.SWITCH.getCode();
                    nodeComponent = (NodeComponent) ReflectUtil.newInstanceIfPossible(c);
                    flowConfiguration.addNodeComponent(id, nodeComponent);
                } else if (NodeComponent.class.isAssignableFrom(c)) {
                    type = NodeTypeEnum.COMMON.getCode();
                    nodeComponent = (NodeComponent) ReflectUtil.newInstanceIfPossible(c);
                    flowConfiguration.addNodeComponent(id, nodeComponent);
                }

                //再尝试声明式组件这部分的推断
                if (type == null) {
                    LiteflowCmpDefine liteflowCmpDefine = AnnotationUtil.getAnnotation(c, LiteflowCmpDefine.class);
                    if (liteflowCmpDefine != null) {
                        type = NodeTypeEnum.COMMON.getCode();
                        Object bean = ReflectUtil.newInstanceIfPossible(c);
                        ComponentProxy proxy = new ComponentProxy(id, bean, NodeComponent.class);
                        nodeComponent = (NodeComponent) proxy.getProxy();
                        flowConfiguration.addNodeComponent(id, nodeComponent);
                    }
                }

                if (type == null) {
                    LiteflowSwitchCmpDefine liteflowSwitchCmpDefine = AnnotationUtil.getAnnotation(c, LiteflowSwitchCmpDefine.class);
                    if (liteflowSwitchCmpDefine != null) {
                        type = NodeTypeEnum.SWITCH.getCode();
                        Object bean = ReflectUtil.newInstanceIfPossible(c);
                        ComponentProxy proxy = new ComponentProxy(id, bean, NodeComponent.class);
                        nodeComponent = (NodeComponent) proxy.getProxy();
                        flowConfiguration.addNodeComponent(id, nodeComponent);
                    }
                }
            } catch (Exception e) {
                throw new NodeClassNotFoundException(StrUtil.format("cannot find the node[{}]", clazz));
            }
        }

        //因为脚本节点是必须设置type的，所以到这里type就全都有了，所以进行二次检查
        if (StrUtil.isBlank(type)) {
            throw new NodeTypeCanNotGuessException(StrUtil.format("cannot guess the type of node[{}]", clazz));
        }

        //检查nodeType是不是规定的类型
        NodeTypeEnum nodeTypeEnum = NodeTypeEnum.getEnumByCode(type);
        if (ObjectUtil.isNull(nodeTypeEnum)) {
            throw new NodeTypeNotSupportException(StrUtil.format("type [{}] is not support", type));
        }

        if (nodeComponent == null) {
            throw new NodeClassNotFoundException(StrUtil.format("cannot find the node[{}]", clazz));
        }

        //进行node的build过程
        return LiteFlowNodeBuilder.createNode(flowConfiguration)
                .setId(id)
                .setInstance(nodeComponent)
                .setName(name)
                .setType(nodeTypeEnum)
                .build();
    }

}
