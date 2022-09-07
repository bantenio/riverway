package com.yomahub.liteflow.parser.base;

import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.builder.ParseResource;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;

import java.util.ArrayList;
import java.util.List;

abstract public class ObjectResourceFlowParser<T> extends BaseFlowParser{

    @Override
    public void parse(List<ParseResource> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {
        List<ObjectResource<T>> objectResources = new ArrayList<>(contentList.size());
        for (ParseResource parseResource : contentList) {
            objectResources.add(resourceToObjectResource(parseResource, flowConfiguration));
        }
        parseObject(objectResources, liteflowConfig, flowConfiguration);
    }

    abstract protected ObjectResource<T> resourceToObjectResource(ParseResource parseResource, FlowConfiguration flowConfiguration);

    abstract public void parseObject(List<ObjectResource<T>> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException;
}
