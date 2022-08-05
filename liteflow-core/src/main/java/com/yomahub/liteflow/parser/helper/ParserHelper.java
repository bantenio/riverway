package com.yomahub.liteflow.parser.helper;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowSwitchCmpDefine;
import com.yomahub.liteflow.builder.LiteFlowChainBuilder;
import com.yomahub.liteflow.builder.LiteFlowConditionBuilder;
import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.builder.prop.ChainPropBean;
import com.yomahub.liteflow.builder.prop.NodePropBean;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.core.proxy.ComponentProxy;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.*;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static com.yomahub.liteflow.common.ChainConstant.*;

/**
 * Parser 通用 Helper
 *
 * @author tangkc
 */
public class ParserHelper {

    /**
     * 私有化构造器
     */
    private ParserHelper() {
    }

    /**
     * 构建 node
     *
     * @param nodePropBean 构建 node 的中间属性
     */
    public static void buildNode(NodePropBean nodePropBean, FlowConfiguration flowConfiguration) {
        String id = nodePropBean.getId();
        String name = nodePropBean.getName();
        String clazz = nodePropBean.getClazz();
        String type = null;

        NodeComponent nodeComponent = null;
        // ! TODO 修改为多种NodeComponent对象创建机制
        //先尝试自动推断类型
        if (StrUtil.isNotBlank(clazz)) {
            try {
                //先尝试从继承的类型中推断
                Class<?> c = Class.forName(clazz);
                if (NodeSwitchComponent.class.isAssignableFrom(c)) {
                    type = NodeTypeEnum.SWITCH.getCode();
                    nodeComponent = (NodeComponent) ReflectUtil.newInstanceIfPossible(c);
                } else if(NodeComponent.class.isAssignableFrom(c)) {
                    type = NodeTypeEnum.COMMON.getCode();
                    nodeComponent = (NodeComponent) ReflectUtil.newInstanceIfPossible(c);
                }

                //再尝试声明式组件这部分的推断
                if (type == null) {
                    LiteflowCmpDefine liteflowCmpDefine = AnnotationUtil.getAnnotation(c, LiteflowCmpDefine.class);
                    if (liteflowCmpDefine != null) {
                        type = NodeTypeEnum.COMMON.getCode();
                        Object bean = ReflectUtil.newInstanceIfPossible(c);
                        ComponentProxy proxy = new ComponentProxy(id, bean, NodeComponent.class);
                        nodeComponent = (NodeComponent) proxy.getProxy();
                    }
                }

                if (type == null) {
                    LiteflowSwitchCmpDefine liteflowSwitchCmpDefine = AnnotationUtil.getAnnotation(c, LiteflowSwitchCmpDefine.class);
                    if (liteflowSwitchCmpDefine != null) {
                        type = NodeTypeEnum.SWITCH.getCode();
                        Object bean = ReflectUtil.newInstanceIfPossible(c);
                        ComponentProxy proxy = new ComponentProxy(id, bean, NodeComponent.class);
                        nodeComponent = (NodeComponent) proxy.getProxy();
                    }
                }
            } catch (Exception e) {
                throw new NodeClassNotFoundException(StrUtil.format("cannot find the node[{}]", clazz));
            }
        }

        //因为脚本节点是必须设置type的，所以到这里type就全都有了，所以进行二次检查
        if (StrUtil.isBlank(type)) {
            throw new NodeTypeCanNotGuessException(StrUtil.format("cannot guess the type of node[{}]", clazz));
        }

        //检查nodeType是不是规定的类型
        NodeTypeEnum nodeTypeEnum = NodeTypeEnum.getEnumByCode(type);
        if (ObjectUtil.isNull(nodeTypeEnum)) {
            throw new NodeTypeNotSupportException(StrUtil.format("type [{}] is not support", type));
        }

        if (nodeComponent == null) {
            throw new NodeClassNotFoundException(StrUtil.format("cannot find the node[{}]", clazz));
        }

        //进行node的build过程
        LiteFlowNodeBuilder.createNode(flowConfiguration)
                .setId(id)
                .setInstance(nodeComponent)
                .setName(name)
                .setType(nodeTypeEnum)
                .build();
    }

    /**
     * 构建 chain
     *
     * @param chainPropBean 构建 chain 的中间属性
     * @param chainBuilder  chainBuilder
     */
    public static Chain buildChain(ChainPropBean chainPropBean,
                                   LiteFlowChainBuilder chainBuilder,
                                   FlowConfiguration flowConfiguration) {
        String condValueStr = chainPropBean.getCondValueStr();
        String group = chainPropBean.getGroup();
        String errorResume = chainPropBean.getErrorResume();
        String any = chainPropBean.getAny();
        String threadExecutorName = chainPropBean.getThreadExecutorName();
        ConditionTypeEnum conditionType = chainPropBean.getConditionType();

        if (ObjectUtil.isNull(conditionType)) {
            throw new NotSupportConditionException("ConditionType is not supported");
        }

        if (StrUtil.isBlank(condValueStr)) {
            throw new EmptyConditionValueException("Condition value cannot be empty");
        }

        Chain chain;
        //如果是when类型的话，有特殊化参数要设置，只针对于when的
        if (conditionType.equals(ConditionTypeEnum.TYPE_WHEN)) {
            chain = chainBuilder.setCondition(
                    LiteFlowConditionBuilder.createWhenCondition(flowConfiguration)
                            .setErrorResume(errorResume)
                            .setGroup(group)
                            .setAny(any)
                            .setThreadExecutorName(threadExecutorName)
                            .setValue(condValueStr)
                            .build()
            ).build();
        } else {
            chain = chainBuilder.setCondition(
                    LiteFlowConditionBuilder.createCondition(conditionType, flowConfiguration)
                            .setValue(condValueStr)
                            .build()
            ).build();
        }
        return chain;
    }

    /**
     * xml 形式的主要解析过程
     *
     * @param documentList          documentList
     * @param chainNameSet          用于去重
     * @param parseOneChainConsumer parseOneChain 函数
     */
    public static FlowConfiguration parseDocument(List<Document> documentList,
                                                  Set<String> chainNameSet,
                                                  BiConsumer<FlowConfiguration, Element> parseOneChainConsumer,
                                                  LiteflowConfig liteflowConfig) {
        FlowConfiguration flowConfiguration = new FlowConfiguration(liteflowConfig);
        //先在元数据里放上chain
        //先放有一个好处，可以在parse的时候先映射到FlowBus的chainMap，然后再去解析
        //这样就不用去像之前的版本那样回归调用
        //同时也解决了不能循环依赖的问题
        documentList.forEach(document -> {
            // 解析chain节点
            List<Element> chainList = document.getRootElement().elements(CHAIN);

            //先在元数据里放上chain
            chainList.forEach(e -> {
                //校验加载的 chainName 是否有重复的
                //TODO 这里是否有个问题，当混合格式加载的时候，2个同名的Chain在不同的文件里，就不行了
                String chainName = e.attributeValue(NAME);
                if (!chainNameSet.add(chainName)) {
                    throw new ChainDuplicateException(String.format("[chain name duplicate] chainName=%s", chainName));
                }

                flowConfiguration.addChain(chainName);
            });
        });
        // 清空
        chainNameSet.clear();

        for (Document document : documentList) {
            Element rootElement = document.getRootElement();
            Element nodesElement = rootElement.element(NODES);
            // 当存在<nodes>节点定义时，解析node节点
            if (ObjectUtil.isNotNull(nodesElement)) {
                List<Element> nodeList = nodesElement.elements(NODE);
                String id, name, clazz, type, script, file;
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

                    ParserHelper.buildNode(nodePropBean, flowConfiguration);
                }
            }

            //解析每一个chain
            List<Element> chainList = rootElement.elements(CHAIN);
            chainList.forEach(element -> parseOneChainConsumer.accept(flowConfiguration, element));
        }
        return flowConfiguration;
    }

    /**
     * json 形式的主要解析过程
     *
     * @param flowJsonObjectList    flowJsonObjectList
     * @param chainNameSet          用于去重
     * @param parseOneChainConsumer parseOneChain 函数
     */
    public static void parseJsonObject(List<JSONObject> flowJsonObjectList,
                                       Set<String> chainNameSet,
                                       BiConsumer<FlowConfiguration, JSONObject> parseOneChainConsumer,
                                       LiteflowConfig liteflowConfig) {
        FlowConfiguration flowConfiguration = new FlowConfiguration(liteflowConfig);
        //先在元数据里放上chain
        //先放有一个好处，可以在parse的时候先映射到FlowBus的chainMap，然后再去解析
        //这样就不用去像之前的版本那样回归调用
        //同时也解决了不能循环依赖的问题
        flowJsonObjectList.forEach(jsonObject -> {
            // 解析chain节点
            JSONArray chainArray = jsonObject.getJSONObject(FLOW).getJSONArray(CHAIN);

            //先在元数据里放上chain
            chainArray.forEach(o -> {
                JSONObject innerJsonObject = (JSONObject) o;
                //校验加载的 chainName 是否有重复的
                // TODO 这里是否有个问题，当混合格式加载的时候，2个同名的Chain在不同的文件里，就不行了
                String chainName = innerJsonObject.getString(NAME);
                if (!chainNameSet.add(chainName)) {
                    throw new ChainDuplicateException(String.format("[chain name duplicate] chainName=%s", chainName));
                }

                flowConfiguration.addChain(innerJsonObject.getString(NAME));
            });
        });
        // 清空
        chainNameSet.clear();

        for (JSONObject flowJsonObject : flowJsonObjectList) {
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

                    ParserHelper.buildNode(nodePropBean, flowConfiguration);
                }
            }

            //解析每一个chain
            JSONArray chainArray = flowJsonObject.getJSONObject(FLOW).getJSONArray(CHAIN);
            chainArray.forEach(o -> {
                JSONObject jsonObject = (JSONObject) o;
                parseOneChainConsumer.accept(flowConfiguration, jsonObject);
            });
        }
    }

    /**
     * 解析一个chain的过程
     *
     * @param chainObject chain 节点
     */
    public static void parseOneChain(JSONObject chainObject, FlowConfiguration flowConfiguration) {
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
                    .setConditionType(conditionType);

            // 构建 chain
            ParserHelper.buildChain(chainPropBean, chainBuilder, flowConfiguration);
        }
    }

    /**
     * 解析一个chain的过程
     * <p>
     * param e chain 节点
     */
    public static void parseOneChain(Element e, FlowConfiguration flowConfiguration) {
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
            ParserHelper.buildChain(chainPropBean, chainBuilder, flowConfiguration);
        }
    }

    /**
     * 解析一个chain的过程
     *
     * @param chainObject chain 节点
     */
    public static void parseOneChainEl(JSONObject chainObject, FlowConfiguration flowConfiguration) {
        //构建chainBuilder
        String chainName = chainObject.getString(NAME);
        String el = chainObject.getString(VALUE);
        LiteFlowChainELBuilder chainELBuilder = LiteFlowChainELBuilder.createChain(flowConfiguration).setChainName(chainName);
        chainELBuilder.setEL(el).build();
    }

    /**
     * 解析一个chain的过程
     *
     * @param e chain 节点
     */
    public static void parseOneChainEl(Element e, FlowConfiguration flowConfiguration) {
        //构建chainBuilder
        String chainName = e.attributeValue(NAME);
        String text = e.getText();
        String el = RegexUtil.removeComments(text);
        LiteFlowChainELBuilder chainELBuilder = LiteFlowChainELBuilder.createChain(flowConfiguration).setChainName(chainName);
        chainELBuilder.setEL(el).build();
    }

    private static class RegexUtil {
        // java 注释的正则表达式
        private static final String REGEX_COMMENT = "/\\*((?!\\*/).|[\\r\\n])*?\\*/|[ \\t]*//.*";

        /**
         * 移除 el 表达式中的注释，支持 java 的注释，包括单行注释、多行注释，
         * 会压缩字符串，移除空格和换行符
         *
         * @param elStr el 表达式
         * @return 移除注释后的 el 表达式
         */
        private static String removeComments(String elStr) {
            if (StrUtil.isBlank(elStr)) {
                return elStr;
            }

            String text = Pattern.compile(REGEX_COMMENT)
                    .matcher(elStr)
                    // 移除注释
                    .replaceAll(CharSequenceUtil.EMPTY)
                    // 移除字符串中的空格
                    .replaceAll(CharSequenceUtil.SPACE, CharSequenceUtil.EMPTY);

            // 移除所有换行符
            return StrUtil.removeAllLineBreaks(text);
        }
    }


}
