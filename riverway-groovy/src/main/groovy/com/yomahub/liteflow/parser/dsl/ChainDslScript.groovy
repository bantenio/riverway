package com.yomahub.liteflow.parser.dsl

import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.builder.prop.NodePropBean
import com.yomahub.liteflow.components.RefValueHandler
import com.yomahub.liteflow.components.ThrowComponent
import com.yomahub.liteflow.components.ValueHandler
import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.core.NodeSwitchComponent
import com.yomahub.liteflow.enums.ConditionTypeEnum
import com.yomahub.liteflow.enums.NodeTypeEnum
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.Executable
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.*
import com.yomahub.liteflow.parser.grf.GyfFileFlowParser
import com.yomahub.liteflow.property.LiteFlowConfig
import groovy.transform.TypeChecked

import java.nio.file.Path

@TypeChecked
abstract class ChainDslScript extends Script {

    private FlowConfiguration flowConfiguration

    private Binding binding

    private GyfFileFlowParser parser;

    private Path parentPath

    void __init__() {
        this.binding = this.getBinding()
        this.flowConfiguration = (FlowConfiguration) this.binding.getVariable("flowConfiguration")
        this.parser = (GyfFileFlowParser) binding.getVariable("parser")
        this.parentPath = (Path) binding.getVariable("parentPath")
        this.binding.removeVariable("flowConfiguration")
        this.binding.removeVariable("parser")
        this.binding.removeVariable("parentPath")
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
        WhenCondition condition = new WhenCondition()
        for (Executable executable : executables) {
            opIdx++
            if (executable == null) {
                String msg = CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx);
                throw new LiteFlowParseException(msg)
            }
            condition.addExecutable(executable)
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
        if (node.getNode().getInstance().getType() != NodeTypeEnum.SWITCH) {
            throw new LiteFlowParseException(StrUtil.format("Component {} is not NodeSwitchComponent", node.getCurrChainName()))
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

    NodeCondition node(Class<? extends NodeComponent> clazz) {
        def beanName = StrUtil.lowerFirst(clazz.simpleName)
        return node(beanName, clazz)
    }
    static Class<NodeSwitchComponent> SWITCH_COMPONENT_CLASS = NodeSwitchComponent.class

    NodeCondition node(String id, Class<? extends NodeComponent> clazz) {
        def clazzName = clazz.simpleName
        def beanName = StrUtil.lowerFirst(clazzName)
        def nodePropBean = new NodePropBean()
                .setClazz(clazzName)
                .setId(beanName)
        def type = clazz.isAssignableFrom(SWITCH_COMPONENT_CLASS) ? NodeTypeEnum.SWITCH : NodeTypeEnum.COMMON
        if (nodePropBean != null) {
            def node = new Node(id, beanName, type, clazzName)
            def component = flowConfiguration.getNodeComponent(id)
            if (component != null) {
                node.setInstance(component)
            }
            flowConfiguration.getNodeManager().addNode(id, beanName, type, clazzName)
            return new NodeCondition(node)
        }
        throw new LiteFlowParseException("Not create the component info from class ${clazz.getName()}")
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
        return new RefValueHandler(expression, type)
    }

    NodeCondition Throw(Throwable throwable, String name) {
        if (throwable == null) {
            throw new LiteFlowParseException("Throw func throwable must be not null")
        }
        Node node = new Node(new ThrowComponent(throwable))
        node.setName(name)
        return new NodeCondition(node)
    }

    NodeCondition Throw(ValueHandler valueHandler, String name) {
        if (valueHandler == null) {
            throw new LiteFlowParseException("Throw func valueHandler must be not null")
        }
        Node node = new Node(new ThrowComponent(valueHandler))
        node.setName(name)
        return new NodeCondition(node)
    }

    Chain chain(String chainName, Closure<Condition> defineFunc) {
        flowConfiguration.addChain(chainName)
        defineFunc.delegate = this
        def chain = flowConfiguration.getChain(chainName)
        def condition = defineFunc()
        if (condition != null) {
            def preConditionList = new ArrayList<Condition>()
            def finallyConditionList = new ArrayList<Condition>()
            for (executable in condition.executableList) {
                if (executable instanceof PreCondition) {
                    preConditionList.add(executable)
                } else if (executable instanceof FinallyCondition) {
                    finallyConditionList.add(executable)
                }
            }
            def conditionList = new ArrayList<Condition>()
            conditionList.add(condition)
            chain.setConditionList(conditionList)
            chain.setPreConditionList(preConditionList)
            chain.setFinallyConditionList(finallyConditionList)
        }
        return chain
    }

    Chain include(String path) {
        return parser.parseChain(path, flowConfiguration, parentPath)
    }

}
