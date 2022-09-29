package com.yomahub.liteflow.parser.base;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.builder.LiteFlowChainBuilder;
import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.builder.ParseResource;
import com.yomahub.liteflow.builder.prop.ChainPropBean;
import com.yomahub.liteflow.builder.prop.NodePropBean;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.ChainDuplicateException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.yomahub.liteflow.common.ChainConstant.*;


/**
 * 基类，用于存放 XmlFlowParser 通用方法
 *
 * @author tangkc
 */
public abstract class BaseXmlFlowParser extends ObjectResourceFlowParser<Document> {
    private final Set<String> CHAIN_NAME_SET = new HashSet<>();

    @Override
    protected ObjectResource<Document> resourceToObjectResource(ParseResource parseResource, FlowConfiguration flowConfiguration) {
        try {
            ObjectResource<Document> objectResource = new ObjectResource<>();
            objectResource.setContent(parseResource.getContent())
                    .setResource(parseResource.getResource());
            return objectResource.setObject(DocumentHelper.parseText(parseResource.getContent()));
        } catch (DocumentException e) {
            throw new LiteFlowParseException(StrUtil.format("parse xml {} error", parseResource.getResource()), e);
        }
    }

    @Override
    public void parseObject(List<ObjectResource<Document>> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        parseDocument(contentList, CHAIN_NAME_SET, liteflowConfig, flowConfiguration);
    }

    /**
     * xml 形式的主要解析过程
     *
     * @param documentList documentList
     * @param chainNameSet 用于去重
     */
    public FlowConfiguration parseDocument(List<ObjectResource<Document>> documentList,
                                           Set<String> chainNameSet,
                                           LiteFlowConfig liteflowConfig,
                                           FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        //先在元数据里放上chain
        //先放有一个好处，可以在parse的时候先映射到FlowBus的chainMap，然后再去解析
        //这样就不用去像之前的版本那样回归调用
        //同时也解决了不能循环依赖的问题
        for (ObjectResource<Document> objectResource : documentList) {
            Document document = objectResource.getObject();
            // 解析chain节点
            List<Element> chainList = document.getRootElement().elements(CHAIN);
            for (Element e : chainList) {
                //校验加载的 chainName 是否有重复的
                //TODO 这里是否有个问题，当混合格式加载的时候，2个同名的Chain在不同的文件里，就不行了
                String chainName = e.attributeValue(NAME);
                if (!chainNameSet.add(chainName)) {
                    throw new ChainDuplicateException(String.format("[chain name duplicate] chainName=%s", chainName));
                }

                flowConfiguration.addChain(chainName);
            }
        }
        // 清空
        chainNameSet.clear();

        for (ObjectResource<Document> objectResource : documentList) {
            Document document = objectResource.getObject();
            Element rootElement = document.getRootElement();
            Element nodesElement = rootElement.element(NODES);
            // 当存在<nodes>节点定义时，解析node节点
            if (ObjectUtil.isNotNull(nodesElement)) {
                List<Element> nodeList = nodesElement.elements(NODE);
                String id, name, clazz, type;
                for (Element e : nodeList) {
                    id = e.attributeValue(ID);
                    name = e.attributeValue(NAME);
                    clazz = e.attributeValue(_CLASS);
                    type = e.attributeValue(TYPE);

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
            List<Element> chainList = rootElement.elements(CHAIN);
            for (Element element : chainList) {
                parseOneChain(flowConfiguration, element, objectResource);
            }
        }
        return flowConfiguration;
    }

    public void parseOneChain(FlowConfiguration flowConfiguration, Element e, ObjectResource<Document> objectResource) {
        String condValueStr;
        String group;
        String errorResume;
        String any;
        String threadExecutorName;
        ConditionTypeEnum conditionType;

        //构建chainBuilder
        String chainName = e.attributeValue(NAME);
        LiteFlowChainBuilder chainBuilder = LiteFlowChainBuilder.createChain(flowConfiguration).setChainName(chainName);

        for (Iterator<Element> it = e.elementIterator(); it.hasNext(); ) {
            Element condE = it.next();
            conditionType = ConditionTypeEnum.getEnumByCode(condE.getName());
            condValueStr = condE.attributeValue(VALUE);
            errorResume = condE.attributeValue(ERROR_RESUME);
            group = condE.attributeValue(GROUP);
            any = condE.attributeValue(ANY);
            threadExecutorName = condE.attributeValue(THREAD_EXECUTOR_NAME);

            ChainPropBean chainPropBean = new ChainPropBean()
                    .setCondValueStr(condValueStr)
                    .setGroup(group)
                    .setErrorResume(errorResume)
                    .setAny(any)
                    .setThreadExecutorName(threadExecutorName)
                    .setConditionType(conditionType);

            // 构建 chain
            buildChain(chainPropBean, chainBuilder, flowConfiguration, objectResource);
        }
    }

}
