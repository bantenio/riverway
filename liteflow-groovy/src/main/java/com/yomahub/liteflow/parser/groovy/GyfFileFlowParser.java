package com.yomahub.liteflow.parser.groovy;

import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.builder.ParseResource;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.base.BaseFlowParser;
import com.yomahub.liteflow.parser.base.ObjectResource;
import com.yomahub.liteflow.property.LiteFlowConfig;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.List;

public class GyfFileFlowParser extends BaseFlowParser<Object> {
    @Override
    protected ObjectResource<Object> resourceToObjectResource(ParseResource parseResource, FlowConfiguration flowConfiguration) {
        Binding binding = new Binding();
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(ProjectScript.class.getName());
        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, config);

        return null;
    }

    @Override
    public void parseObject(List<ObjectResource<Object>> contentList, LiteFlowConfig liteflowConfig, FlowConfiguration flowConfiguration) throws LiteFlowParseException {

    }
}
