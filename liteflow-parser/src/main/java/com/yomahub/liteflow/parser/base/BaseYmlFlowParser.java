package com.yomahub.liteflow.parser.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.yaml.snakeyaml.Yaml;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基类，用于存放 YmlFlowParser 通用方法
 *
 * @author tangkc
 */
public abstract class BaseYmlFlowParser extends BaseJsonFlowParser {

    private final Set<String> CHAIN_NAME_SET = new HashSet<>();

    @Override
    public void parse(List<String> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        if (CollectionUtil.isEmpty(contentList)) {
            return;
        }

        List<JSONObject> jsonObjectList = ListUtil.toList();
        for (String content : contentList) {
            JSONObject ruleObject = convertToJson(content);
            jsonObjectList.add(ruleObject);
        }

        parseJsonObject(jsonObjectList, CHAIN_NAME_SET, liteflowConfig, flowConfiguration);
    }

    protected JSONObject convertToJson(String yamlString) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(yamlString);
        return JSON.parseObject(JSON.toJSONString(map));
    }
}
