package com.yomahub.liteflow.parser

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.io.resource.ResourceUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.*
import com.yomahub.liteflow.exception.ConfigErrorException
import com.yomahub.liteflow.exception.NotSupportParseWayException
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.parser.grf.GyfFileFlowParser
import com.yomahub.liteflow.property.LiteFlowConfig

class ResourceGyfFlowParser implements UrlFlowParser {
    private static final String GYF_CLASS_PATH_URL_PREFIX = "gyf:classpath://";
    private static final String GYF_ALL_CLASS_PATH_URL_PREFIX = "gyf:classpath*://";

    private static final String[] SUPPORT_PARSE_WAY = new String[]{GYF_CLASS_PATH_URL_PREFIX, GYF_ALL_CLASS_PATH_URL_PREFIX};

    public static void register() {
        FlowParserProvider.register(new ResourceGyfFlowParser());
    }

    @Override
    public void parseMain(String path, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        String prefix = getType(path);
        String[] parts = prefix.split(":");
        if (parts.length < 3 || StrUtil.isBlank(prefix)) {
            throw new LiteFlowParseException("path was not support format: " + path);
        }
        path = path.substring(prefix.length());
        path = parts[1] + ':' + path;
        parseContent(path, new GyfFileFlowParser(), flowConfiguration, liteflowConfig);
    }

    protected void parseContent(String path, FlowParser flowParser, FlowConfiguration flowConfiguration, LiteFlowConfig liteFlowConfig) {

        if (StrUtil.isEmpty(path)) {
            throw new ConfigErrorException("rule source must not be null");
        }
        List<ParseResource> allResource = flowConfiguration.getResourceParser().findResources(path);
        if (allResource.isEmpty()) {
            throw new ConfigErrorException("config error,please check rule source property");
        }
        //如果有多个资源，检查资源都是同一个类型，如果出现不同类型的配置，则抛出错误提示
        Set<String> fileTypeSet = new HashSet<>();
        try {
            for (ParseResource resource : allResource) {
                fileTypeSet.add(FileUtil.extName(resource.getResourcePath().toURL().getFile()));
            }
        } catch (Exception e) {
            throw new ConfigErrorException("config error,please check rule source property", e);
        }
        if (fileTypeSet.size() != 1) {
            throw new ConfigErrorException("config error,please use the same type of configuration");
        }
        flowParser.parse(allResource, liteFlowConfig, flowConfiguration);
    }

    @Override
    public boolean acceptsURL(String url) throws LiteFlowParseException {
        return startsWithIgnoreCaseAny(url, SUPPORT_PARSE_WAY) != null;
    }

    @Override
    public void parse(List<ParseResource> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        throw new NotSupportParseWayException();
    }

    protected String getType(String path) {
        String prefix = startsWithIgnoreCaseAny(path, SUPPORT_PARSE_WAY);
        if (prefix == null) {
            throw new LiteFlowParseException("不支持的资源路径：" + path);
        }
        return prefix;
    }

    public static String startsWithIgnoreCaseAny(String str, String... searchStrings) {
        if (str == null || searchStrings == null) {
            return null;
        }
        for (String searchString : searchStrings) {
            if (StrUtil.startWithIgnoreCase(str, searchString)) {
                return searchString;
            }
        }
        return null;
    }
}