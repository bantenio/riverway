package com.yomahub.liteflow.parser.base;

import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.dom4j.Element;

import java.util.*;

/**
 * 虽则Parser的抽象类，所有的parser需要继承这个抽象类
 * @author guodongqing
 * @since 2.5.0
 */
public interface FlowParser {

    void parseMain(List<String> pathList, LiteflowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws Exception;

    void parse(List<String> contentList, LiteflowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws Exception;

}
