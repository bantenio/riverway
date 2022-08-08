package com.yomahub.liteflow.parser.factory;

import com.yomahub.liteflow.builder.FlowParser;
import com.yomahub.liteflow.builder.LiteFlowParseException;

public interface UrlFlowParser extends FlowParser {

    boolean acceptsURL(String url) throws LiteFlowParseException;
}
