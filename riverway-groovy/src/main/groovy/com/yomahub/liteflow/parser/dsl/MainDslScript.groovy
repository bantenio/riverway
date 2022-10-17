package com.yomahub.liteflow.parser.dsl

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.parser.dsl.define.PathChain

abstract class MainDslScript extends Script {

    Set<String> chainPaths = new LinkedHashSet<>(10)

    private FlowConfiguration flowConfiguration

    private Binding binding

    private Map<String, PathChain> mappingChains = new HashMap<>()

    void __init__(boolean includeChainAndNode = false) {
        this.binding = this.getBinding()
        this.flowConfiguration = (FlowConfiguration) this.binding.getVariable("flowConfiguration")
        this.binding.removeVariable("flowConfiguration")
        this.binding.removeVariable("parser")
        this.binding.removeVariable("parentPath")
    }

    void chain(String chainPath) {
        chainPaths.add(chainPath)
    }

    void chain(String chainPath, String name) {
        chainPaths.add(chainPath)
        PathChain pathChain = new PathChain().setChainPath(chainPath)
        pathChain.chainName(name)
        flowConfiguration.addChain(pathChain)
        mappingChains.put(chainPath, pathChain)
    }

    void node(String name) {
        if (flowConfiguration.containNode(name)) {
            return
        }
        NodeComponent nodeComponent = flowConfiguration.getNodeComponent(name)
        if (nodeComponent == null) {
            throw new LiteFlowParseException(StrUtil.format("Not found NodeComponent {} ", name))
        }
        flowConfiguration.addNode(name, name, nodeComponent)
    }

    Map<String, Chain> getMappingChains() {
        return this.mappingChains
    }
}
