package com.yomahub.liteflow.plugins.gyf

import com.yomahub.liteflow.plugins.Interceptor
import com.yomahub.liteflow.plugins.SubPluginManage
import com.yomahub.liteflow.util.CopyOnWriteHashMap

class GyfChainBuilderSubPluginManage implements SubPluginManage<GyfChainBuilderInterceptor> {

    public static final String PLUGIN_MANAGE_NAME = "gyf_chain_builder";

    private final Map<String, GyfChainBuilderInterceptor> interceptorMap = new CopyOnWriteHashMap<>();

    @Override
    void process(String name, Interceptor interceptor) {
        if (interceptor instanceof GyfChainBuilderInterceptor && !contains(name)) {
            interceptorMap.put(name, (GyfChainBuilderInterceptor) interceptor);
        }

    }

    @Override
    GyfChainBuilderInterceptor get(String name) {
        return interceptorMap.get(name);
    }

    @Override
    boolean contains(String name) {
        return interceptorMap.containsKey(name);
    }

    @Override
    void unregister(String name) {
        interceptorMap.remove(name);
    }

    @Override
    int size() {
        return interceptorMap.size();
    }

    @Override
    boolean isEmpty() {
        return interceptorMap.isEmpty();
    }

    @Override
    String getName() {
        return PLUGIN_MANAGE_NAME;
    }

    @Override
    Collection<GyfChainBuilderInterceptor> getRegisters() {
        return interceptorMap.values();
    }
}