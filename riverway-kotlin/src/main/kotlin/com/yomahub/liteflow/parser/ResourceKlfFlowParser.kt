package com.yomahub.liteflow.parser

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.*
import com.yomahub.liteflow.exception.ConfigErrorException
import com.yomahub.liteflow.exception.NotSupportParseWayException
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.parser.kotlin.klf.KlfFileFlowParser
import com.yomahub.liteflow.property.LiteFlowConfig

class ResourceKlfFlowParser : UrlFlowParser {
    @Throws(LiteFlowParseException::class)
    override fun parseMain(path: String, liteflowConfig: LiteFlowConfig, flowConfiguration: FlowConfiguration) {
        var path = path
        val prefix = getType(path)
        val parts = prefix.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size < 3 || StrUtil.isBlank(prefix)) {
            throw LiteFlowParseException("path was not support format: $path")
        }
        path = path.substring(prefix.length)
        //        path = parts[1] + ':' + path;
        parseContent(path, KlfFileFlowParser(), flowConfiguration, liteflowConfig)
    }

    protected fun parseContent(
        path: String?,
        flowParser: FlowParser,
        flowConfiguration: FlowConfiguration,
        liteFlowConfig: LiteFlowConfig?
    ) {
        if (StrUtil.isEmpty(path)) {
            throw ConfigErrorException("rule source must not be null")
        }
        val allResource = flowConfiguration.resourceParser.findResources(path)
        if (allResource.isEmpty()) {
            throw ConfigErrorException("config error,please check rule source property")
        }
        //如果有多个资源，检查资源都是同一个类型，如果出现不同类型的配置，则抛出错误提示
        val fileTypeSet: MutableSet<String> = HashSet()
        try {
            for (resource in allResource) {
                fileTypeSet.add(FileUtil.extName(resource.resourcePath.toURL().file))
            }
        } catch (e: Exception) {
            throw ConfigErrorException("config error,please check rule source property", e)
        }
        if (fileTypeSet.size != 1) {
            throw ConfigErrorException("config error,please use the same type of configuration")
        }
        flowParser.parse(allResource, liteFlowConfig, flowConfiguration)
    }

    @Throws(LiteFlowParseException::class)
    override fun acceptsURL(url: String): Boolean {
        return startsWithIgnoreCaseAny(url, *SUPPORT_PARSE_WAY) != null
    }

    @Throws(LiteFlowParseException::class)
    override fun parse(
        contentList: List<ParseResource>,
        liteflowConfig: LiteFlowConfig,
        flowConfiguration: FlowConfiguration
    ) {
        throw NotSupportParseWayException()
    }

    protected fun getType(path: String): String {
        return startsWithIgnoreCaseAny(path, *SUPPORT_PARSE_WAY)
            ?: throw LiteFlowParseException("不支持的资源路径：$path")
    }

    companion object {
        private const val KLF_CLASS_PATH_URL_PREFIX = "klf:classpath://"
        private const val KLF_ALL_CLASS_PATH_URL_PREFIX = "klf:classpath://"
        private val SUPPORT_PARSE_WAY = arrayOf(KLF_CLASS_PATH_URL_PREFIX, KLF_ALL_CLASS_PATH_URL_PREFIX)
        fun register() {
            FlowParserProvider.register(ResourceKlfFlowParser())
        }

        fun startsWithIgnoreCaseAny(str: String?, vararg searchStrings: String?): String? {
            if (str == null || searchStrings == null) {
                return null
            }
            for (searchString in searchStrings) {
                if (StrUtil.startWithIgnoreCase(str, searchString)) {
                    return searchString
                }
            }
            return null
        }
    }
}
