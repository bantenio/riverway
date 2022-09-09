package com.yomahub.liteflow.parser.grf

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.builder.ParseResource
import com.yomahub.liteflow.builder.gyf.prop.GyfChainPropBean
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.Executable
import com.yomahub.liteflow.flow.element.condition.Condition
import com.yomahub.liteflow.flow.element.condition.FinallyCondition
import com.yomahub.liteflow.flow.element.condition.PreCondition
import com.yomahub.liteflow.parser.base.BaseFlowParser
import com.yomahub.liteflow.parser.dsl.ChainDslScript
import com.yomahub.liteflow.parser.dsl.MainDslScript
import com.yomahub.liteflow.plugins.SubPluginManage
import com.yomahub.liteflow.plugins.gyf.AddFuncOrOperationInterceptorContext
import com.yomahub.liteflow.plugins.gyf.AddImportsInterceptorContext
import com.yomahub.liteflow.plugins.gyf.GyfChainBuilderInterceptor
import com.yomahub.liteflow.plugins.gyf.GyfChainBuilderSubPluginManage
import com.yomahub.liteflow.property.LiteFlowConfig
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Path
import java.nio.file.Paths

class GyfFileFlowParser extends BaseFlowParser {

    @Override
    void parse(List<ParseResource> contentList, LiteFlowConfig liteFlowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        for (ParseResource parseResource : contentList) {
            Binding binding = new Binding()
            CompilerConfiguration config = new CompilerConfiguration()
            config.setScriptBaseClass(MainDslScript.class.getName())
            GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, config)

            binding.setVariable("flowConfiguration", flowConfiguration)
            binding.setVariable("baseFlowParser", this)
            binding.setVariable("parseResource", parseResource)

            //这里一定要先放chain，再放node，因为node优先于chain，所以当重名时，node会覆盖掉chain
            //往上下文里放入所有的chain，是的el表达式可以直接引用到chain
            for (Chain chain : flowConfiguration.getChainMap().values()) {
                binding.setVariable(chain.getChainName(), chain)
            }

            //往上下文里放入所有的node，使得el表达式可以直接引用到nodeId
            for (String nodeId : flowConfiguration.getNodeMap().keySet()) {
                binding.setVariable(nodeId, flowConfiguration.getNode(nodeId))
            }
            MainDslScript script = (MainDslScript) shell.parse(parseResource.getContent())
            script.run()
            Set<GyfChainPropBean> paths = script.chainPaths
            if (paths.isEmpty()) {
                throw new LiteFlowParseException(StrUtil.format("The resource {} not defined chain", parseResource.getResource()))
            }
            String parentPathValue = parseResource.getResourcePath()
            Path parentPath = Paths.get(parentPathValue).getParent()
            for (final def path in paths) {
                parseChain(path, flowConfiguration, parseResource, parentPath)
            }
        }
    }

    protected void parseChain(GyfChainPropBean gyfChainPropBean, FlowConfiguration flowConfiguration, ParseResource parent, Path parentPath) throws LiteFlowParseException {
        Path chainPath = parentPath.resolve(gyfChainPropBean.getChainPath())
        ParseResource parseResource = new ParseResource()
                .setResourcePath(chainPath.toString())
                .setResource(gyfChainPropBean.getChainPath())
                .setContent(FileUtil.readUtf8String(chainPath.toString()))
        Binding binding = new Binding()
        CompilerConfiguration config = new CompilerConfiguration()
        config.setScriptBaseClass(ChainDslScript.class.getName())

        binding.setVariable("flowConfiguration", flowConfiguration)

        SubPluginManage<?> subPluginManage = flowConfiguration.getPluginManager().getPluginManage(GyfChainBuilderSubPluginManage.PLUGIN_MANAGE_NAME)
        if (subPluginManage != null && !subPluginManage.isEmpty()) {
            ImportCustomizer importCustomizer = new ImportCustomizer()
            AddImportsInterceptorContext interceptorContext = new AddImportsInterceptorContext(flowConfiguration, binding, importCustomizer)
            AddFuncOrOperationInterceptorContext addFuncInterceptorContext = new AddFuncOrOperationInterceptorContext(flowConfiguration)
            GyfChainBuilderSubPluginManage chainBuilderSubPluginManage = (GyfChainBuilderSubPluginManage) subPluginManage
            for (GyfChainBuilderInterceptor register : chainBuilderSubPluginManage.getRegisters()) {
                register.addImports(interceptorContext)
                register.addFuncOrOperation(addFuncInterceptorContext)
            }
            config.addCompilationCustomizers(importCustomizer)
        }

        for (Chain chain : flowConfiguration.getChainMap().values()) {
            binding.setVariable(chain.getChainName(), chain)
        }

        //往上下文里放入所有的node，使得el表达式可以直接引用到nodeId
        for (String nodeId : flowConfiguration.getNodeMap().keySet()) {
            binding.setVariable(nodeId, flowConfiguration.getNode(nodeId))
        }

        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, config)
        Condition condition = (Condition) shell.evaluate(parseResource.getContent(), parseResource.getResource())

        List<Condition> conditionList = new ArrayList<>()
        List<Condition> preConditionList = new ArrayList<>()
        List<Condition> finallyConditionList = new ArrayList<>()
        for (Executable executable : condition.getExecutableList()) {
            if (executable instanceof PreCondition) {
                preConditionList.add((PreCondition) executable)
            } else if (executable instanceof FinallyCondition) {
                finallyConditionList.add((FinallyCondition) executable)
            }
        }

        //把主要的condition加入
        conditionList.add(condition)

        Chain chain = flowConfiguration.getChain(gyfChainPropBean.chainName)
        chain.setConditionList(conditionList)
        chain.setPreConditionList(preConditionList)
        chain.setFinallyConditionList(finallyConditionList)
        flowConfiguration.addChain(chain)
    }
}