package com.yomahub.liteflow.parser;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.builder.*;
import com.yomahub.liteflow.exception.ConfigErrorException;
import com.yomahub.liteflow.exception.NotSupportParseWayException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.el.JsonELFileFlowParser;
import com.yomahub.liteflow.parser.el.XmlELFileFlowParser;
import com.yomahub.liteflow.parser.el.YamlELFileFlowParser;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceELFlowParser implements UrlFlowParser {
    private static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static final String JSON_CLASS_PATH_URL_PREFIX = "el:json:classpath://";
    private static final String JSON_ALL_CLASS_PATH_URL_PREFIX = "el:json:classpath*://";
    private static final String JSON_FILE_PATH_URL_PREFIX = "el:json:file://";

    private static final String XML_CLASS_PATH_URL_PREFIX = "el:xml:classpath://";
    private static final String XML_ALL_CLASS_PATH_URL_PREFIX = "el:xml:classpath*://";
    private static final String XML_FILE_PATH_URL_PREFIX = "el:xml:file://";

    private static final String YAML_CLASS_PATH_URL_PREFIX = "el:yaml:classpath://";
    private static final String YAML_XML_ALL_CLASS_PATH_URL_PREFIX = "el:yaml:classpath*://";
    private static final String YAML_XML_FILE_PATH_URL_PREFIX = "el:yaml:file://";

    private static final String[] SUPPORT_PARSE_WAY = new String[]{
            JSON_CLASS_PATH_URL_PREFIX, JSON_ALL_CLASS_PATH_URL_PREFIX, JSON_FILE_PATH_URL_PREFIX,
            XML_CLASS_PATH_URL_PREFIX, XML_ALL_CLASS_PATH_URL_PREFIX, XML_FILE_PATH_URL_PREFIX,
            YAML_CLASS_PATH_URL_PREFIX, YAML_XML_ALL_CLASS_PATH_URL_PREFIX, YAML_XML_FILE_PATH_URL_PREFIX
    };

    private JsonELFileFlowParser jsonELFileFlowParser = new JsonELFileFlowParser();

    private XmlELFileFlowParser xmlELFileFlowParser = new XmlELFileFlowParser();

    private YamlELFileFlowParser yamlELFileFlowParser = new YamlELFileFlowParser();

    private Map<String, FlowParser> parserMap = new HashMap<>();

    public static void register() {
        FlowParserProvider.register(new ResourceELFlowParser());
    }

    public ResourceELFlowParser() {
        parserMap.put("json", jsonELFileFlowParser);
        parserMap.put("xml", xmlELFileFlowParser);
        parserMap.put("yaml", yamlELFileFlowParser);
    }

    protected void parseContent(String path, String prefix, String resourcePathType, FlowParser flowParser, FlowConfiguration flowConfiguration, LiteFlowConfig liteFlowConfig) {
        path = path.substring(prefix.length());
        parseContent(resourcePathType + ':' + path, flowParser, flowConfiguration, liteFlowConfig);
    }

    public void parseContent(String path, FlowParser flowParser, FlowConfiguration flowConfiguration, LiteFlowConfig liteFlowConfig) {
        if (StrUtil.isEmpty(path)) {
            throw new ConfigErrorException("rule source must not be null");
        }

        List<Resource> allResource = parseContent(path);

        //如果有多个资源，检查资源都是同一个类型，如果出现不同类型的配置，则抛出错误提示
        Set<String> fileTypeSet = new HashSet<>();
        allResource.forEach(resource -> fileTypeSet.add(FileUtil.extName(resource.getFilename())));
        if (fileTypeSet.size() != 1) {
            throw new ConfigErrorException("config error,please use the same type of configuration");
        }

        List<ParseResource> contents = allResource.stream().map(resource -> {
            try {
                return new ParseResource()
                        .setResourcePath(resource.getFile().getPath())
                        .setResource(resource.getFilename())
                        .setContent(IoUtil.read(resource.getInputStream(), CharsetUtil.CHARSET_UTF_8));
            } catch (IOException e) {
                throw new ConfigErrorException("config error,please check rule source property", e);
            }
        }).collect(Collectors.toList());
        flowParser.parse(contents, liteFlowConfig, flowConfiguration);
    }

    protected List<Resource> parseContent(String path) {
        String locationPattern = path;

        try {
            Resource[] resources = resolver.getResources(locationPattern);
            if (ArrayUtil.isEmpty(resources)) {
                throw new ConfigErrorException("config error,please check rule source property");
            }
            return Arrays.asList(resources);
        } catch (IOException e) {
            throw new ConfigErrorException("config error,please check the path", e);
        }
    }

    @Override
    public void parseMain(String path, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        String prefix = getType(path);
        String[] parts = prefix.split(":");
        if (parts.length < 3 || StrUtil.isBlank(prefix)) {
            throw new LiteFlowParseException("path was not support format: " + path);
        }
        FlowParser parser = parserMap.get(parts[1]);
        parseContent(path, prefix, parts[2], parser, flowConfiguration, liteflowConfig);
    }

    @Override
    public boolean acceptsURL(String url) throws LiteFlowParseException {
        return startsWithIgnoreCaseAny(url, SUPPORT_PARSE_WAY) != null;
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

    @Override
    public void parse(List<ParseResource> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        throw new NotSupportParseWayException();
    }
}
