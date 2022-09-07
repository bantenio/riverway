package com.yomahub.liteflow.parser.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.yomahub.liteflow.builder.FlowParser;
import com.yomahub.liteflow.builder.LiteFlowChainBuilder;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.builder.ParseResource;
import com.yomahub.liteflow.builder.prop.ChainPropBean;
import com.yomahub.liteflow.builder.prop.NodePropBean;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.ChainDuplicateException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.yomahub.liteflow.common.ChainConstant.*;


/**
 * 基类，用于存放 JsonFlowParser 通用方法
 *
 * @author tangkc
 */
public abstract class BaseJsonFlowParser extends ObjectResourceFlowParser<JSONObject> {

    private final Set<String> CHAIN_NAME_SET = new CopyOnWriteArraySet<>();

    @Override
    protected ObjectResource<JSONObject> resourceToObjectResource(ParseResource parseResource, FlowConfiguration flowConfiguration) {
        JSONObject flowJsonObject = JSONObject.parseObject(parseResource.getContent(), Feature.OrderedField);
        ObjectResource<JSONObject> objectResource = new ObjectResource<>();
        objectResource.setContent(parseResource.getContent())
                .setResource(parseResource.getResource());
        return objectResource.setObject(flowJsonObject);
    }

    @Override
    public void parseObject(List<ObjectResource<JSONObject>> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        parseJsonObject(contentList, CHAIN_NAME_SET, liteflowConfig, flowConfiguration);
    }


    /**
     * json 形式的主要解析过程
     *
     * @param chainNameSet 用于去重
     */
    public void parseJsonObject(List<ObjectResource<JSONObject>> contentList,
                                Set<String> chainNameSet,
                                LiteFlowConfig liteflowConfig,
                                FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        //先在元数据里放上chain
        //先放有一个好处，可以在parse的时候先映射到FlowBus的chainMap，然后再去解析
        //这样就不用去像之前的版本那样回归调用
        //同时也解决了不能循环依赖的问题
        for (ObjectResource<JSONObject> jsonObjectObjectResource : contentList) {
            JSONArray chainArray = jsonObjectObjectResource.getObject().getJSONObject(FLOW).getJSONArray(CHAIN);
            for (Object chain : chainArray) {
                JSONObject innerJsonObject = (JSONObject) chain;
                //校验加载的 chainName 是否有重复的
                // TODO 这里是否有个问题，当混合格式加载的时候，2个同名的Chain在不同的文件里，就不行了
                String chainName = innerJsonObject.getString(NAME);
                if (!chainNameSet.add(chainName)) {
                    throw new ChainDuplicateException(String.format("[chain name duplicate] chainName=%s", chainName));
                }

                flowConfiguration.addChain(innerJsonObject.getString(NAME));
            }
        }
        // 清空
        chainNameSet.clear();

        for (ObjectResource<JSONObject> objectResource : contentList) {
            JSONObject flowJsonObject = objectResource.getObject();
            // 当存在<nodes>节点定义时，解析node节点
            if (flowJsonObject.getJSONObject(FLOW).containsKey(NODES)) {
                JSONArray nodeArrayList = flowJsonObject.getJSONObject(FLOW).getJSONObject(NODES).getJSONArray(NODE);
                String id, name, clazz, type;
                for (int i = 0; i < nodeArrayList.size(); i++) {
                    JSONObject nodeObject = nodeArrayList.getJSONObject(i);
                    id = nodeObject.getString(ID);
                    name = nodeObject.getString(NAME);
                    clazz = nodeObject.getString(_CLASS);
                    type = nodeObject.getString(TYPE);

                    // 构建 node
                    NodePropBean nodePropBean = new NodePropBean()
                            .setId(id)
                            .setName(name)
                            .setClazz(clazz)
                            .setType(type);

                    buildNode(nodePropBean, flowConfiguration);
                }
            }

            //解析每一个chain
            JSONArray chainArray = flowJsonObject.getJSONObject(FLOW).getJSONArray(CHAIN);
            for (Object chain : chainArray) {
                JSONObject jsonObject = (JSONObject) chain;
                parseOneChain(flowConfiguration, jsonObject, objectResource);
            }
        }
    }

    /**
     * 解析一个chain的过程
     *
     * @param chainObject chain 节点
     */
    public void parseOneChain(FlowConfiguration flowConfiguration, JSONObject chainObject, ObjectResource<JSONObject> objectResource) {
        String condValueStr;
        ConditionTypeEnum conditionType;
        String group;
        String errorResume;
        String any;
        String threadExecutorName;

        //构建chainBuilder
        String chainName = chainObject.getString(NAME);
        LiteFlowChainBuilder chainBuilder = LiteFlowChainBuilder.createChain(flowConfiguration).setChainName(chainName);

        for (Object o : chainObject.getJSONArray(CONDITION)) {
            JSONObject condObject = (JSONObject) o;
            conditionType = ConditionTypeEnum.getEnumByCode(condObject.getString(TYPE));
            condValueStr = condObject.getString(VALUE);
            errorResume = condObject.getString(ERROR_RESUME);
            group = condObject.getString(GROUP);
            any = condObject.getString(ANY);
            threadExecutorName = condObject.getString(THREAD_EXECUTOR_NAME);

            ChainPropBean chainPropBean = new ChainPropBean()
                    .setCondValueStr(condValueStr)
                    .setGroup(group)
                    .setErrorResume(errorResume)
                    .setAny(any)
                    .setThreadExecutorName(threadExecutorName)
                    .setConditionType(conditionType)
                    .setChainName(chainName);

            // 构建 chain
            buildChain(chainPropBean, chainBuilder, flowConfiguration, objectResource);
        }
    }

}
