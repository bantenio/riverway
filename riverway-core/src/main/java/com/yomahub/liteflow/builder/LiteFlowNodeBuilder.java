package com.yomahub.liteflow.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.NodeBuildException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LiteFlowNodeBuilder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Node node;

    private final FlowConfiguration flowConfiguration;

    public static LiteFlowNodeBuilder createNode(FlowConfiguration flowConfiguration) {
        return new LiteFlowNodeBuilder(flowConfiguration);
    }

    public static LiteFlowNodeBuilder createCommonNode(FlowConfiguration flowConfiguration) {
        return createNode(flowConfiguration).setType(NodeTypeEnum.COMMON);
    }

    public static LiteFlowNodeBuilder createSwitchNode(FlowConfiguration flowConfiguration) {
        return createNode(flowConfiguration).setType(NodeTypeEnum.COMMON);
    }

    public LiteFlowNodeBuilder(FlowConfiguration flowConfiguration) {
        this.node = new Node();
        this.flowConfiguration = flowConfiguration;
    }

    public LiteFlowNodeBuilder setId(String nodeId) {
        if (StrUtil.isBlank(nodeId)) {
            return this;
        }
        this.node.setId(nodeId.trim());
        return this;
    }

    public LiteFlowNodeBuilder setName(String name) {
        if (StrUtil.isBlank(name)) {
            return this;
        }
        this.node.setName(name.trim());
        return this;
    }

    public LiteFlowNodeBuilder setType(NodeTypeEnum type) {
        this.node.setType(type);
        return this;
    }

    public NodeComponent getInstance() {
        return node.getInstance();
    }

    public LiteFlowNodeBuilder setInstance(NodeComponent instance) {
        node.setInstance(instance);
        return this;
    }

    public Node build() {
        checkBuild();
        try {
            return flowConfiguration.addNode(this.node.getId(), this.node.getName(), this.node.getInstance());
        } catch (Exception e) {
            String errMsg = StrUtil.format("An exception occurred while building the node[{}]", this.node.getId());
            throw new NodeBuildException(errMsg);
        }
    }

    /**
     * build 前简单校验
     */
    private void checkBuild() {
        List<String> errorList = new ArrayList<>();
        if (StrUtil.isBlank(this.node.getId())) {
            errorList.add("id is blank");
        }
        if (Objects.isNull(this.node.getType())) {
            errorList.add("type is null");
        }
        if (CollUtil.isNotEmpty(errorList)) {
            throw new NodeBuildException(CollUtil.join(errorList, ",", "[", "]"));
        }
    }
}
