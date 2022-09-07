package com.yomahub.liteflow.parser.dsl

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.ParseResource
import com.yomahub.liteflow.builder.prop.ChainPropBean
import com.yomahub.liteflow.builder.prop.NodePropBean
import com.yomahub.liteflow.builder.gyf.prop.GyfChainPropBean
import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.enums.ConditionTypeEnum
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.parser.base.BaseFlowParser
import org.codehaus.groovy.runtime.DefaultGroovyMethods

abstract class MainDslScript extends Script {

    private FlowConfiguration flowConfiguration

    private BaseFlowParser baseFlowParser

    private ParseResource parseResource

    private Set<GyfChainPropBean> chainPaths = new HashSet<>(10);

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

    Set<GyfChainPropBean> __process_chains__() {
        chainPaths;
    }

    void node(@DelegatesTo(NodePropBean.class) Closure<NodePropBean> closure) {
        NodePropBean nodePropBean = new NodePropBean()
        DefaultGroovyMethods.with(nodePropBean, closure)
        baseFlowParser.buildNode(nodePropBean, flowConfiguration)
    }

    void node(String id, String name = null, String clazz = null, String type = null) {
        node({
            setId id
            setName name
            setClazz clazz
            setType type
        })
    }

    void node(Class<? extends NodeComponent> clazz) {
        def simpleName = clazz.getSimpleName()
        simpleName = StrUtil.lowerFirst(simpleName)
        node { ->
            setId simpleName
            setClazz clazz.getName()
        }
    }

    void chain(String path, @DelegatesTo(ChainPropBean.class) Closure<ChainPropBean> closure) {
        ChainPropBean chainPropBean = new ChainPropBean()
        DefaultGroovyMethods.with(chainPropBean, closure)
        String chainName = chainPropBean.getChainName()
        if (flowConfiguration.containChain(chainName)) {
            throw new RuntimeException(StrUtil.format("the {} chain define is duplication", chainName))
        }
        flowConfiguration.addChain(chainName)
//        LiteFlowChainBuilder chainBuilder = LiteFlowChainBuilder.createChain(flowConfiguration).setChainName(chainName)
//
//        // 构建 chain
//        baseFlowParser.buildChain(chainPropBean, chainBuilder, flowConfiguration, parseResource)
        chainPaths.add(new GyfChainPropBean(chainPath: path, chainPropBean: chainPropBean))
    }

    void chain(String chainPath,
               String chainName,
               String group = null,
               ConditionTypeEnum conditionType = null,
               String errorResume = null,
               String threadExecutorName = null,
               String any = null) {
        chain(chainPath, {
            setChainName chainName
            setGroup group
            setConditionType conditionType
            setErrorResume errorResume
            setThreadExecutorName threadExecutorName
            setAny any
        })
    }
}
