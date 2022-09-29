package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.SubPluginManage;
import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.Collection;
import java.util.Map;

public class NodeComponentExecutorPluginManage implements SubPluginManage<NodeComponentExecuteInterceptor> {
    public static final String PLUGIN_MANAGE_NAME = "node_component_executor";

    private final Map<String, NodeComponentExecuteInterceptor> interceptorMap = new CopyOnWriteHashMap<>();


    public Collection<NodeComponentExecuteInterceptor> getRegisters() {
        return interceptorMap.values();
    }

    @Override
    public void process(String name, Interceptor interceptor) {
        if (interceptor instanceof NodeComponentExecuteInterceptor && !contains(name)) {
            interceptorMap.put(name, (NodeComponentExecuteInterceptor) interceptor);
        }
    }

    @Override
    public NodeComponentExecuteInterceptor get(String name) {
        return interceptorMap.get(name);
    }

    @Override
    public boolean contains(String name) {
        return interceptorMap.containsKey(name);
    }

    @Override
    public void unregister(String name) {
        interceptorMap.remove(name);
    }

    public int size() {
        return interceptorMap.size();
    }

    public boolean isEmpty() {
        return interceptorMap.isEmpty();
    }

    @Override
    public String getName() {
        return PLUGIN_MANAGE_NAME;
    }
}
