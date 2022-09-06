package com.yomahub.liteflow.parser.el;

import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.base.BaseYmlFlowParser;
import com.yomahub.liteflow.parser.base.ObjectResource;

import static com.yomahub.liteflow.common.ChainConstant.NAME;
import static com.yomahub.liteflow.common.ChainConstant.VALUE;

/**
 * yml形式的EL表达式解析抽象引擎
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class YamlELFileFlowParser extends BaseYmlFlowParser {

    /**
     * 解析一个chain的过程
     *
     * @param chainObject chain 节点
     */
    @Override
    public void parseOneChain(FlowConfiguration flowConfiguration, JSONObject chainObject, ObjectResource<JSONObject> objectResource) {
        parseOneChainEl(chainObject, flowConfiguration, objectResource);
    }

    public void parseOneChainEl(JSONObject chainObject, FlowConfiguration flowConfiguration, ObjectResource<JSONObject> objectResource) {
        //构建chainBuilder
        String chainName = chainObject.getString(NAME);
        String el = chainObject.getString(VALUE);
        LiteFlowChainELBuilder chainELBuilder = LiteFlowChainELBuilder
                .createChain(flowConfiguration)
                .setChainName(chainName);
        chainELBuilder.setEL(el, objectResource).build();
    }
}
