package com.yomahub.liteflow.parser.dsl

import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.builder.gyf.operator.ext.LogNodeAround
import com.yomahub.liteflow.components.RefValueHandler
import com.yomahub.liteflow.components.ThrowComponent
import com.yomahub.liteflow.components.ValueHandler
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.Executable
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.*
import com.yomahub.liteflow.property.LiteFlowConfig
import org.slf4j.event.Level

abstract class ChainDslScript extends Script {

    private FlowConfiguration flowConfiguration

    private Binding binding

    void __init__() {
        binding = this.getBinding()
        this.flowConfiguration = (FlowConfiguration) binding.getVariable("flowConfiguration")
        binding.removeVariable("flowConfiguration")
        Condition.metaClass.id = id
        ChainProxy.metaClass.id = id
        SwitchCondition.metaClass.to = to
        Map.metaClass.add = { k, v ->
            delegate.put(k, v)
            return delegate
        }
        NodeCondition.metaClass.property = { k, v ->
            delegate.addProperty(k, v)
            return delegate
        }
        NodeCondition.metaClass.swap = { String k, String v ->
            if (StrUtil.isBlank(k) || StrUtil.isBlank(v)) {
                throw new LiteFlowParseException("swap func key or value must be not null or empty")
            }
            delegate.addSwapHandler(k, v)
            return delegate
        }
        NodeCondition.metaClass.swap = { String k, ValueHandler v ->
            if (StrUtil.isBlank(k) || v == null) {
                throw new LiteFlowParseException("swap func key or value must be not null or empty")
            }
            delegate.addSwapHandler(k, v)
            return delegate
        }
        Node.metaClass.tag = { String tag ->
            if (StrUtil.isBlank(tag)) {
                throw new LiteFlowParseException("tag func key or value must be not null or empty")
            }
            Node copyNode = flowConfiguration.copyNode(delegate.getId())
            copyNode.setTag(tag)
            return copyNode
        }
        Condition.metaClass.ignoreError = { boolean val ->
            delegate.setErrorResume(val)
            return delegate
        }
        WhenCondition.metaClass.threadPool = { String poolName ->
            if (StrUtil.isBlank(poolName)) {
                throw new LiteFlowParseException("threadPool func poolName must be not null or empty")
            }
            delegate.setThreadExecutorName(poolName)
            return delegate
        }
        NodeCondition.metaClass.log = { Level level, String message, Object... args ->
            if (level == null) {
                throw new LiteFlowParseException("log func level must be not null")
            }
            if (StrUtil.isBlank(message)) {
                throw new LiteFlowParseException("log func message must be not null or empty")
            }
            delegate.addNodeAroundCondition(new LogNodeAround(message, args, level))
            return delegate
        }
    }

    ThenCondition THEN(Executable... executables) throws Throwable {
        if (executables.length == 0) {
            throw new LiteFlowParseException("THEN executable list is empty.");
        }
        int opIdx = -1;
        ThenCondition condition = new ThenCondition();
        for (Executable executable : executables) {
            opIdx++
            if (executable == null) {
                String msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx);
                throw new Exception(msg)
            }
            condition.addExecutable(executable);
        }
        return condition;
    }

    WhenCondition WHEN(Executable... executables) throws Throwable {
        if (executables.length == 0) {
            throw new LiteFlowParseException("WHEN executable list is empty.");
        }
        int opIdx = -1;
        WhenCondition condition = new WhenCondition(flowConfiguration.getLiteflowConfig());
        for (Executable executable : executables) {
            opIdx++;
            if (executable == null) {
                String msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx);
                throw new LiteFlowParseException(msg);
            }
            condition.addExecutable(executable);
        }
        return condition;
    }

    SwitchCondition SWITCH(Node node) throws Throwable {
        if (node == null) {
            throw new LiteFlowParseException("SWITCH parameter is null");
        }

        SwitchCondition switchCondition = new SwitchCondition();
        switchCondition.setSwitchNode(node);
        return switchCondition;
    }

    SwitchCondition SWITCH(NodeCondition node) throws Throwable {
        if (node == null) {
            throw new LiteFlowParseException("SWITCH parameter is null");
        }

        SwitchCondition switchCondition = new SwitchCondition();
        switchCondition.setSwitchNode(node);
        return switchCondition;
    }

    PreCondition PRE(Executable... executables) throws Throwable {
        if (executables.length == 0) {
            throw new LiteFlowParseException("PRE executable list is empty.");
        }
        int opIdx = -1;
        PreCondition condition = new PreCondition();
        for (Executable executable : executables) {
            opIdx++;
            if (executable == null) {
                String msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx);
                throw new LiteFlowParseException(msg);
            }
            condition.addExecutable(executable);
        }
        return condition;
    }

    FinallyCondition FINALLY(Executable... executables) throws Throwable {
        if (executables.length == 0) {
            throw new LiteFlowParseException("FINALLY executable list is empty.");
        }
        int opIdx = -1;
        FinallyCondition condition = new FinallyCondition();
        for (Executable executable : executables) {
            opIdx++;
            if (executable == null) {
                String msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx);
                throw new LiteFlowParseException(msg);
            }
            condition.addExecutable(executable);
        }
        return condition;
    }

    NodeCondition node(String nodeId) {
        if (StrUtil.isBlank(nodeId)) {
            throw new LiteFlowParseException("node function the nodeId must be Node item!");
        }
        if (flowConfiguration.containNode(nodeId)) {
            return new NodeCondition(flowConfiguration.getNode(nodeId));
        } else {
            LiteFlowConfig liteFlowConfig = flowConfiguration.getLiteflowConfig()
            String substituteCmpClass = liteFlowConfig.getNodeComponentProperties().getSubstituteCmpClass()
            if (StrUtil.isNotBlank(substituteCmpClass)) {
                Node substituteNode = flowConfiguration.getNodeMap().values().stream().filter(node
                        -> node.getInstance().getClass().getName() == substituteCmpClass).findFirst().orElse(null)
                if (ObjectUtil.isNotNull(substituteNode)) {
                    return new NodeCondition(substituteNode)
                } else {
                    String error = StrUtil.format("This node[{}] cannot be found", nodeId)
                    throw new LiteFlowParseException(error);
                }
            } else {
                String error = StrUtil.format("This node[{}] cannot be found, or you can configure an substitute node", nodeId)
                throw new LiteFlowParseException(error);
            }
        }
    }

    NodeCondition node(Node node) {
        if (node == null) {
            throw new NullPointerException("node function the value must be Node item!");
        }
        return new NodeCondition(node)
    }

    Map<String, Object> map() {
        return new HashMap<String, Object>()
    }

    RefValueHandler ref(String expression, String type = null) {
        if (StrUtil.isBlank(expression)) {
            throw new LiteFlowParseException("ref func message must be not null or empty")
        }
        String target = flowConfiguration.getLiteflowConfig().getRefType()
        if (StrUtil.isNotBlank(type)) {
            target = type
        }
        return new RefValueHandler(expression, target)
    }

    NodeCondition Throw(Throwable throwable, String name) {
        if (throwable == null) {
            throw new LiteFlowParseException("Throw func throwable must be not null")
        }
        Node node = new Node(new ThrowComponent(throwable), flowConfiguration)
        node.setName(name)
        return new NodeCondition(node)
    }

    NodeCondition Throw(ValueHandler valueHandler, String name) {
        if (valueHandler == null) {
            throw new LiteFlowParseException("Throw func valueHandler must be not null")
        }
        Node node = new Node(new ThrowComponent(valueHandler), flowConfiguration)
        node.setName(name)
        return new NodeCondition(node)
    }

    String strFmt(String message, Object... args) {
        if (StrUtil.isBlank(message)) {
            throw new NullPointerException("strFmt function the message must be blank!");
        }
        StrUtil.format(message, args)
    }

    def to = { Executable... executables ->
        for (Executable arg : executables) {
            if (arg == null) {
                throw new RuntimeException("The parameter must be Executable item!");
            }
            delegate.addTargetItem((Executable) arg);
        }
        return delegate
    }

    def id = { String id ->
        delegate.setId(id)
        return delegate
    }
}
