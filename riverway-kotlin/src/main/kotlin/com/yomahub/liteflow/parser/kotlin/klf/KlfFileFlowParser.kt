package com.yomahub.liteflow.parser.kotlin.klf

import com.yomahub.liteflow.builder.LiteFlowParseException
import com.yomahub.liteflow.builder.ParseResource
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.parser.base.BaseFlowParser
import com.yomahub.liteflow.property.LiteFlowConfig
import javax.script.Bindings
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings

internal class KlfFileFlowParser : BaseFlowParser() {
    @Throws(LiteFlowParseException::class)
    override fun parse(
        contentList: List<ParseResource>,
        liteflowConfig: LiteFlowConfig,
        flowConfiguration: FlowConfiguration
    ) {
        val binding: Bindings = SimpleBindings()
        try {
            val scriptEngine = ScriptEngineManager().getEngineByExtension("main.kts")
            if (contentList != null) {
                for (parseResource in contentList) {
                    scriptEngine.eval(parseResource.content, binding)
                }
            }
            println(binding)
        } catch (e: Exception) {
            throw LiteFlowParseException("parse chain file on error.", e)
        }
    }
}