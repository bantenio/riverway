package com.yomahub.liteflow.parser.el;

import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;

import java.util.List;

/**
 * 基于自定义的xml方式EL表达式解析器
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public abstract class ClassXmlFlowELParser extends XmlFlowELParser{
    @Override
    public void parseMain(List<String> pathList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        String content = parseCustom();
        parse(content, liteflowConfig, flowConfiguration);
    }

    public abstract String parseCustom();
}
