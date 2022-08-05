package com.yomahub.liteflow.parser.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.helper.ParserHelper;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.yaml.snakeyaml.Yaml;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 基类，用于存放 YmlFlowParser 通用方法
 *
 * @author tangkc
 */
public abstract class BaseYmlFlowParser implements FlowParser {

    private final Set<String> CHAIN_NAME_SET = new HashSet<>();

    public void parse(String content, LiteflowConfig liteflowConfig) throws Exception {
        parse(ListUtil.toList(content), liteflowConfig);
    }

    @Override
    public void parse(List<String> contentList, LiteflowConfig liteflowConfig) throws Exception {
        if (CollectionUtil.isEmpty(contentList)) {
            return;
        }

        List<JSONObject> jsonObjectList = ListUtil.toList();
        for (String content : contentList) {
            JSONObject ruleObject = convertToJson(content);
            jsonObjectList.add(ruleObject);
        }

        ParserHelper.parseJsonObject(jsonObjectList, CHAIN_NAME_SET, this::parseOneChain, liteflowConfig);
    }

    protected JSONObject convertToJson(String yamlString) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(yamlString);
        return JSON.parseObject(JSON.toJSONString(map));
    }

    /**
     * 解析一个 chain 的过程
     *
     * @param chain chain
     */
    public abstract void parseOneChain(FlowConfiguration flowConfiguration, JSONObject chain);
}
