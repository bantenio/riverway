package com.yomahub.liteflow.builder;

import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;

import java.util.List;

/**
 * 虽则Parser的抽象类，所有的parser需要继承这个抽象类
 * @author guodongqing
 * @since 2.5.0
 */
public interface FlowParser {

    void parse(List<String> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException;
}
