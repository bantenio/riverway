package com.yomahub.liteflow.builder;

import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;

import java.util.List;

public interface UrlFlowParser extends FlowParser {

    void parseMain(String path, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException;
    default void parseMain(List<String> paths, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        for (String path : paths) {
            parseMain(path, liteflowConfig, flowConfiguration);
        }
    }

    boolean acceptsURL(String url) throws LiteFlowParseException;
}
