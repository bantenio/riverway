package com.yomahub.liteflow.parser.gyf

import cn.hutool.core.io.IoUtil
import cn.hutool.core.io.resource.ResourceUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.builder.ParseResource
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.Chain
import com.yomahub.liteflow.flow.element.condition.Condition
import com.yomahub.liteflow.flow.element.condition.FinallyCondition
import com.yomahub.liteflow.flow.element.condition.PreCondition
import com.yomahub.liteflow.parser.base.BaseFlowParser
import com.yomahub.liteflow.parser.dsl.ChainDslScript
import com.yomahub.liteflow.parser.dsl.MainDslScript
import com.yomahub.liteflow.parser.dsl.define.PathChain
import com.yomahub.liteflow.property.LiteFlowConfig
import groovy.transform.TypeChecked
import org.codehaus.groovy.control.CompilerConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Path
import java.nio.file.Paths

@TypeChecked
class GyfFileFlowParser extends BaseFlowParser {
    private static Logger log = LoggerFactory.getLogger(GyfFileFlowParser.class)

    private HashMap<String, Chain<? extends Chain<?>>> parsedPaths = new LinkedHashMap<>()

    private Map<String, Chain<? extends Chain<?>>> mappingChains = null;

    @Override
    void parse(List<ParseResource> contentList, LiteFlowConfig liteFlowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        for (ParseResource parseResource : contentList) {
            CompilerConfiguration config = new CompilerConfiguration()
            config.setScriptBaseClass(MainDslScript.class.getName())

            URI parentPathValue = parseResource.getResourcePath()
            Path parentPath = Paths.get(parentPathValue).getParent()

            //这里一定要先放chain，再放node，因为node优先于chain，所以当重名时，node会覆盖掉chain
            //往上下文里放入所有的chain，是的el表达式可以直接引用到chain
            GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), new Binding(), config)
            MainDslScript script = (MainDslScript) shell.parse(parseResource.getContent(), parseResource.resource)
            script.init(flowConfiguration)
            script.run()
            Set<String> paths = script.chainPaths
            mappingChains = script.getMappingChains()
            if (paths.isEmpty()) {
                throw new LiteFlowParseException(StrUtil.format("The resource {} not defined chain", parseResource.getResource()))
            }
            for (final def path in paths) {
                log.info "main include chain '{}' from '{}'", parseChain(path, flowConfiguration, parentPath).getChainName(), path
            }
        }
    }

    Chain parseChain(String path, FlowConfiguration flowConfiguration, Path parentPath) throws LiteFlowParseException {
        boolean isPathChain = false
        if (isParsed(path)) {
            Chain chain = parsedPaths.get(path)
            chain = chain == null ? mappingChains.get(path) : chain
            if (chain == null) {
                throw new LiteFlowParseException(StrUtil.format("流程循环引用-> {}", path))
            }
            if (chain instanceof PathChain) {
                isPathChain = true
                if (chain.isInited()) {
                    return chain
                }
            }
        }
        parsedPaths.put(path, null)
        Path chainPath = parentPath.resolve(path)
        log.info("will process chain file {}", chainPath.toString())
        InputStream chainResourceStream = ResourceUtil.getStream(chainPath.toString());
        ParseResource parseResource = new ParseResource()
                .setResourcePath(chainPath.toUri())
                .setResource(path)
                .setContent(IoUtil.readUtf8(chainResourceStream))
        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), new Binding(), new CompilerConfiguration())
        ChainDslScript script = (ChainDslScript) shell.parse(parseResource.getContent(), parseResource.getResource());
        script.init(flowConfiguration, this, parentPath)
        Object result = script.run()
        Chain chain = null;
        if (result instanceof Chain) {
            parsedPaths.put(path, result)
            chain = (Chain) result
        } else if (result instanceof Condition && isPathChain) {
            def preConditionList = new ArrayList<Condition>()
            def finallyConditionList = new ArrayList<Condition>()
            for (executable in result.executableList) {
                if (executable instanceof PreCondition) {
                    preConditionList.add(executable)
                } else if (executable instanceof FinallyCondition) {
                    finallyConditionList.add(executable)
                }
            }
            def conditionList = new ArrayList<Condition>()
            conditionList.add(result)
            chain = mappingChains.get(path)
            chain.setConditionList(conditionList)
            chain.setPreConditionList(preConditionList)
            chain.setFinallyConditionList(finallyConditionList)
        }
        return chain
    }

    boolean isParsed(String path) {
        return parsedPaths.containsKey(path) || mappingChains.containsKey(path)
    }
}