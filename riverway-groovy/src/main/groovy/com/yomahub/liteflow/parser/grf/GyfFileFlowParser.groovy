package com.yomahub.liteflow.parser.grf


import cn.hutool.core.io.IoUtil
import cn.hutool.core.io.resource.ResourceUtil
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
import com.yomahub.liteflow.slot.SlotScope
import groovy.transform.TypeChecked
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.script.Bindings
import java.nio.file.Path
import java.nio.file.Paths

@TypeChecked
class GyfFileFlowParser extends BaseFlowParser {
    private static Logger log = LoggerFactory.getLogger(GyfFileFlowParser.class)

    private HashMap<String, Chain> parsedPaths = new LinkedHashMap<>()

    @Override
    void parse(List<ParseResource> contentList, LiteFlowConfig liteFlowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        for (ParseResource parseResource : contentList) {
            CompilerConfiguration config = new CompilerConfiguration()
            config.setScriptBaseClass(MainDslScript.class.getName())

            URI parentPathValue = parseResource.getResourcePath()
            Path parentPath = Paths.get(parentPathValue).getParent()
            Binding binding = new Binding()
            binding.setVariable("flowConfiguration", flowConfiguration)
            binding.setVariable("parser", this)
            binding.setVariable("parentPath", parentPath)

            //这里一定要先放chain，再放node，因为node优先于chain，所以当重名时，node会覆盖掉chain
            //往上下文里放入所有的chain，是的el表达式可以直接引用到chain
            for (Chain chain : flowConfiguration.getChainMap().values()) {
                binding.setVariable(chain.getChainName(), chain)
            }

            //往上下文里放入所有的node，使得el表达式可以直接引用到nodeId
            for (String nodeId : flowConfiguration.getNodeMap().keySet()) {
                binding.setVariable(nodeId, flowConfiguration.getNode(nodeId))
            }
            GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, config)
            MainDslScript script = (MainDslScript) shell.parse(parseResource.getContent())
            script.run()
            Set<String> paths = script.chainPaths
            if (paths.isEmpty()) {
                throw new LiteFlowParseException(StrUtil.format("The resource {} not defined chain", parseResource.getResource()))
            }
            for (final def path in paths) {
                log.info "main include chain '{}' from '{}'", parseChain(path, flowConfiguration, parentPath).getChainName(), path
            }
        }
    }

    Chain parseChain(String path, FlowConfiguration flowConfiguration, Path parentPath) throws LiteFlowParseException {
        if (isParsed(path)) {
            Chain chain = parsedPaths.get(path)
            if (chain == null) {
                throw new LiteFlowParseException(StrUtil.format("流程循环引用-> {}", path))
            }
            return chain
        }
        parsedPaths.put(path, null)
        Path chainPath = parentPath.resolve(path)
        log.info("will process chain file {}", chainPath.toString())
        InputStream chainResourceStream = ResourceUtil.getStream(chainPath.toString());
        ParseResource parseResource = new ParseResource()
                .setResourcePath(chainPath.toUri())
                .setResource(path)
                .setContent(IoUtil.readUtf8(chainResourceStream))
        Binding binding = new Binding()
        binding.setVariable("flowConfiguration", flowConfiguration)
        binding.setVariable("parser", this)
        binding.setVariable("parentPath", parentPath)
        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, new CompilerConfiguration())
        def chain = (Chain) shell.evaluate(parseResource.getContent(), parseResource.getResource())
        parsedPaths.put(path, chain)
        return chain
    }

    boolean isParsed(String path) {
        return parsedPaths.containsKey(path)
    }
}