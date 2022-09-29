package com.yomahub.liteflow.plugins;

import com.yomahub.liteflow.flow.FlowConfiguration;

public class InterceptorContext<S> {
    private final FlowConfiguration flowConfiguration;

    public InterceptorContext(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
    }

    public FlowConfiguration getFlowConfiguration() {
        return flowConfiguration;
    }
}
