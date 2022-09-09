package com.yomahub.liteflow.parser.dsl

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.ParseResource
import com.yomahub.liteflow.builder.prop.ChainPropBean
import com.yomahub.liteflow.builder.prop.NodePropBean
import com.yomahub.liteflow.builder.gyf.prop.GyfChainPropBean
import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.enums.ConditionTypeEnum
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.parser.base.BaseFlowParser
import com.yomahub.liteflow.parser.dsl.define.ChainSpec
import com.yomahub.liteflow.parser.dsl.define.NodeSpec
import org.codehaus.groovy.runtime.DefaultGroovyMethods

abstract class MainDslScript extends Script {

    private FlowConfiguration flowConfiguration

    private BaseFlowParser baseFlowParser

    private ParseResource parseResource

    Set<GyfChainPropBean> chainPaths = new HashSet<>(10)

    LinkedHashMap<String, Node> nodeDefs = []

    private Binding binding

    void __init__() {
        binding = getBinding()
        this.flowConfiguration = (FlowConfiguration) binding.getVariable("flowConfiguration")
        this.baseFlowParser = (BaseFlowParser) binding.getVariable("baseFlowParser")
        this.parseResource = (ParseResource) binding.getVariable("parseResource")
        binding.removeVariable("flowConfiguration")
        binding.removeVariable("baseFlowParser")
        binding.removeVariable("parseResource")
    }

    void node(@DelegatesTo(NodeSpec.class) Closure closure) {
        node null, false, closure
    }

    void node(String idVal, String clazzVal = null, String nameVal = null, String typeVal = null) {
        node null, false, {
            id(idVal)
            clazz(clazzVal)
            name(nameVal)
            type(typeVal)
        }
    }

    void node(Class<? extends NodeComponent> clazz, boolean withId = false, @DelegatesTo(NodeSpec.class) Closure closure = null) {
        NodeSpec nodeSpec = new NodeSpec(nodePropBean: new NodePropBean())
        if (clazz != null) {
            if (withId) {
                nodeSpec.idWithClazz(clazz)
            } else {
                nodeSpec.clazz(clazz)
            }
        }
        if (closure != null) {
            DefaultGroovyMethods.with(nodeSpec, closure)
        }
        Node node = baseFlowParser.buildNode(nodeSpec.nodePropBean, flowConfiguration)
        nodeDefs."$node.id" = node
    }

    void nodeWithId(Class<? extends NodeComponent> clazz) {
        node(clazz, true, null)
    }

    void nodes(List<Class<? extends NodeComponent>> nodeComponentClasses, @DelegatesTo(NodeSpec.class) Closure closure = null) {
        nodeComponentClasses.forEach(clazz -> {
            node(clazz, false, closure)
        })
    }

    void nodesWithId(List<Class<? extends NodeComponent>> nodeComponentClasses, @DelegatesTo(NodeSpec.class) Closure closure = null) {
        nodeComponentClasses.forEach(clazz -> {
            node(clazz, true, closure)
        })
    }

    void chain(@DelegatesTo(ChainSpec.class) Closure closure) {
        ChainSpec chainSpec = new ChainSpec()
        chainSpec.gyfChainPropBean = new GyfChainPropBean()
        chainSpec.gyfChainPropBean.chainPropBean(new ChainPropBean())
        DefaultGroovyMethods.with(chainSpec, closure)
        String chainName = chainSpec.gyfChainPropBean.getChainName()
        if (flowConfiguration.containChain(chainName)) {
            throw new RuntimeException(StrUtil.format("the {} chain define is duplication", chainName))
        }
        flowConfiguration.addChain(chainName)
        chainPaths.add(chainSpec.gyfChainPropBean)
    }

    void chain(String chainPath,
               String chainName,
               String group = null,
               ConditionTypeEnum conditionType = null,
               String errorResume = null,
               String threadExecutorName = null,
               String any = null) {
        if (flowConfiguration.containChain(chainName)) {
            throw new RuntimeException(StrUtil.format("the {} chain define is duplication", chainName))
        }
        var gyfChainPropBean = new GyfChainPropBean()
        gyfChainPropBean.chainPropBean(new ChainPropBean())
        gyfChainPropBean.setChainPath(chainPath)
                .setChainName(chainName)
                .setGroup(group)
                .setConditionType(conditionType)
                .setErrorResume(errorResume)
                .setThreadExecutorName(threadExecutorName)
                .setAny(any)
        flowConfiguration.addChain(chainName)
        chainPaths.add(gyfChainPropBean)
    }
}
