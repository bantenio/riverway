package com.yomahub.liteflow.plugins.el;

import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.SubPluginManage;
import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.Collection;
import java.util.Map;

public class ELChainBuilderSubPluginManage implements SubPluginManage<ChainBuilderInterceptor> {

    public static final String PLUGIN_MANAGE_NAME = "el_chain_builder";

    private final Map<String, ChainBuilderInterceptor> interceptorMap = new CopyOnWriteHashMap<>();

    @Override
    public void process(String name, Interceptor interceptor) {
        if (interceptor instanceof ChainBuilderInterceptor && !contains(name)) {
            interceptorMap.put(name, (ChainBuilderInterceptor) interceptor);
        }

    }

    @Override
    public ChainBuilderInterceptor get(String name) {
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

    @Override
    public int size() {
        return interceptorMap.size();
    }

    @Override
    public boolean isEmpty() {
        return interceptorMap.isEmpty();
    }

    @Override
    public String getName() {
        return PLUGIN_MANAGE_NAME;
    }

    @Override
    public Collection<ChainBuilderInterceptor> getRegisters() {
        return interceptorMap.values();
    }
}
