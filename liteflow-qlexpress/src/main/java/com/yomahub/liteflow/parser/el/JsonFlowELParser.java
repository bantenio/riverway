package com.yomahub.liteflow.parser.el;

import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.base.BaseJsonFlowParser;
import com.yomahub.liteflow.builder.UrlFlowParser;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.yomahub.liteflow.common.ChainConstant.NAME;
import static com.yomahub.liteflow.common.ChainConstant.VALUE;

/**
 * JSON形式的EL表达式解析抽象引擎
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class JsonFlowELParser extends BaseJsonFlowParser implements UrlFlowParser {

    private static final String URL_PREFIX = "el:json://";

    @Override
    public void parseMain(List<String> pathList,
                          LiteFlowConfig liteflowConfig,
                          FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        List<String> contents = new LinkedList<>();
        for (String path : pathList) {
            List<String> pathToContents = pathToContent(path);
            if (pathToContents == null) {
                throw new LiteFlowParseException("the path" + path + " not read any content");
            }
            contents.addAll(pathToContents);
        }
        parse(contents, liteflowConfig, flowConfiguration);
    }

    protected List<String> pathToContent(String path) {
        if (!acceptsURL(path)) {
            throw new LiteFlowParseException("the path" + path + " was not support " + URL_PREFIX + " schema");
        }
        try {
            return Arrays.asList(FileUtils.readFileToString(new File(path.substring(URL_PREFIX.length())), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new LiteFlowParseException(e);
        }
    }

    /**
     * 解析一个chain的过程
     *
     * @param chainObject chain 节点
     */
    @Override
    public void parseOneChain(FlowConfiguration flowConfiguration, JSONObject chainObject) {
        parseOneChainEl(chainObject, flowConfiguration);
    }

    public void parseOneChainEl(JSONObject chainObject, FlowConfiguration flowConfiguration) {
        //构建chainBuilder
        String chainName = chainObject.getString(NAME);
        String el = chainObject.getString(VALUE);
        LiteFlowChainELBuilder chainELBuilder = LiteFlowChainELBuilder.createChain(flowConfiguration).setChainName(chainName);
        chainELBuilder.setEL(el).build();
    }

    @Override
    public boolean acceptsURL(String url) throws LiteFlowParseException {
        return false;
    }
}
