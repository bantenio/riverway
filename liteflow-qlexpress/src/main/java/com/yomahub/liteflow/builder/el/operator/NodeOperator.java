package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ql.util.express.Operator;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的node的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.3
 */
public class NodeOperator extends Operator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final FlowConfiguration flowConfiguration;

    public NodeOperator(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        if (ArrayUtil.isEmpty(objects)) {
            throw new Exception();
        }

        if (objects.length != 1) {
            log.error("parameter error");
            throw new Exception("parameter error");
        }

        String nodeId;
        if (objects[0] instanceof String) {
            nodeId = (String) objects[0];
        } else if (objects[0] instanceof Node) {
            return new NodeCondition((Node) objects[0]);
        } else {
            log.error("The value must be Node item!");
            throw new Exception("The value must be Node item!");
        }

        if (flowConfiguration.containNode(nodeId)) {
            return new NodeCondition(flowConfiguration.getNode(nodeId));
        } else {
            LiteFlowConfig liteflowConfig = flowConfiguration.getLiteflowConfig();
            if (StrUtil.isNotBlank(liteflowConfig.getNodeComponentProperties().getSubstituteCmpClass())) {
                Node substituteNode = flowConfiguration.getNodeMap().values().stream().filter(node
                        -> node.getInstance().getClass().getName().equals(liteflowConfig.getNodeComponentProperties().getSubstituteCmpClass())).findFirst().orElse(null);
                if (ObjectUtil.isNotNull(substituteNode)) {
                    return substituteNode;
                } else {
                    String error = StrUtil.format("This node[{}] cannot be found", nodeId);
                    log.error(error);
                    throw new Exception(error);
                }
            } else {
                String error = StrUtil.format("This node[{}] cannot be found, or you can configure an substitute node", nodeId);
                log.error(error);
                throw new Exception(error);
            }
        }
    }
}
