package com.yomahub.liteflow.parser.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.ParseResource;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * 基类，用于存放 YmlFlowParser 通用方法
 *
 * @author tangkc
 */
public abstract class BaseYmlFlowParser extends BaseJsonFlowParser {

    @Override
    protected ObjectResource<JSONObject> resourceToObjectResource(ParseResource parseResource) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(parseResource.getContent());
        JSONObject flowJsonObject = JSON.parseObject(JSON.toJSONString(map));
        ObjectResource<JSONObject> objectResource = new ObjectResource<>();
        objectResource.setContent(parseResource.getContent())
                .setResource(parseResource.getResource());
        return objectResource.setObject(flowJsonObject);
    }
}
