package com.yomahub.liteflow.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.entity.ExecutableEntity;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.ExecutableItemNotFoundException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.*;

import java.util.List;

/**
 * Condition基于代码形式的组装器
 *
 * @author Bryan.Zhang
 * @since 2.6.8
 */
public class LiteFlowConditionBuilder {

    protected Condition condition;

    private FlowConfiguration flowConfiguration;

    public static LiteFlowConditionBuilder createCondition(ConditionTypeEnum conditionType,
                                                           FlowConfiguration flowConfiguration) {
        switch (conditionType) {
            case TYPE_THEN:
                return createThenCondition(flowConfiguration);
            case TYPE_WHEN:
                return createWhenCondition(flowConfiguration);
            case TYPE_PRE:
                return createPreCondition(flowConfiguration);
            case TYPE_FINALLY:
                return createFinallyCondition(flowConfiguration);
            default:
                return null;
        }
    }

    public static LiteFlowConditionBuilder createThenCondition(FlowConfiguration flowConfiguration) {
        return new LiteFlowConditionBuilder(new ThenCondition()).setFlowConfiguration(flowConfiguration);
    }

    public static LiteFlowWhenConditionBuilder createWhenCondition(FlowConfiguration flowConfiguration) {
        LiteFlowWhenConditionBuilder builder = new LiteFlowWhenConditionBuilder(new WhenCondition());
        builder.setFlowConfiguration(flowConfiguration);
        return builder;
    }

    public static LiteFlowConditionBuilder createPreCondition(FlowConfiguration flowConfiguration) {
        return new LiteFlowConditionBuilder(new PreCondition()).setFlowConfiguration(flowConfiguration);
    }

    public static LiteFlowConditionBuilder createFinallyCondition(FlowConfiguration flowConfiguration) {
        return new LiteFlowConditionBuilder(new FinallyCondition()).setFlowConfiguration(flowConfiguration);
    }

    public LiteFlowConditionBuilder(Condition condition) {
        this.condition = condition;
    }

    public LiteFlowConditionBuilder setValue(String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        String[] condArray = value.split(",");

        RegexEntity regexEntity;
        String itemExpression;
        for (String s : condArray) {
            itemExpression = s.trim();
            regexEntity = RegexEntity.parse(itemExpression);
            // 先转化为执行实体对象
            ExecutableEntity executableEntity = convertExecutableEntity(regexEntity);
            // 构建节点或流程
            setExecutable(executableEntity);
        }
        return this;
    }

    // 将正则表达式实体转化为执行实体
    private ExecutableEntity convertExecutableEntity(RegexEntity regexEntity) {
        RegexNodeEntity item = regexEntity.getItem();
        ExecutableEntity executableEntity = new ExecutableEntity(item.getId(), item.getTag());
        if (ObjectUtil.isNotNull(regexEntity.getRealItemArray())) {
            for (RegexNodeEntity realItem : regexEntity.getRealItemArray()) {
                executableEntity.addNodeCondComponent(new ExecutableEntity(realItem.getId(), realItem.getTag()));
            }
        }
        return executableEntity;
    }

    // 设置执行节点或者流程
    public LiteFlowConditionBuilder setExecutable(ExecutableEntity executableEntity) {
        if (flowConfiguration.containNode(executableEntity.getId())) {
            Node node = flowConfiguration.copyNode(executableEntity.getId());
            node.setTag(executableEntity.getTag());

            //如果没有条件节点，说明是普通组件，如果有条件节点，就去构建SwitchCondition
            if (CollUtil.isEmpty(executableEntity.getNodeCondComponents())) {
                this.condition.getExecutableList().add(node);
            } else {
                buildSwitchNode(node, executableEntity.getNodeCondComponents());
            }
        } else if (hasChain(executableEntity.getId())) {
            Chain chain = flowConfiguration.getChain(executableEntity.getId());
            this.condition.getExecutableList().add(chain);
        }
        return this;
    }

    // 构建条件节点
    private void buildSwitchNode(Node node, List<ExecutableEntity> executableEntities) {
        if (CollUtil.isEmpty(executableEntities)) {
            return;
        }

        SwitchCondition switchCondition = new SwitchCondition();
        switchCondition.setSwitchNode(node);

        for (ExecutableEntity realItem : executableEntities) {
            if (flowConfiguration.containNode(realItem.getId())) {
                Node targetNode = flowConfiguration.copyNode(realItem.getId());
                targetNode.setTag(realItem.getTag());
                switchCondition.addTargetItem(targetNode);
            } else if (hasChain(realItem.getId())) {
                Chain chain = flowConfiguration.getChain(realItem.getId());
                switchCondition.addTargetItem(chain);
            } else {
                String errorMsg = StrUtil.format("executable node[{}] is not found!", realItem.getId());
                throw new ExecutableItemNotFoundException(errorMsg);
            }
        }
        this.condition.getExecutableList().add(switchCondition);
    }

    public Condition build() {
        return this.condition;
    }

    private boolean hasChain(String chainId) {
        return flowConfiguration.containChain(chainId);
    }

    public FlowConfiguration getFlowConfiguration() {
        return flowConfiguration;
    }

    public LiteFlowConditionBuilder setFlowConfiguration(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
        return this;
    }
}
