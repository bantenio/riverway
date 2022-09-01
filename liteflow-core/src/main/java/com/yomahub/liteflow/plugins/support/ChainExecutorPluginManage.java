package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.SubPluginManage;
import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.Collection;
import java.util.Map;

public class ChainExecutorPluginManage implements SubPluginManage<ChainExecuteInterceptor> {

    public static final String PLUGIN_MANAGE_NAME = "chain_executor";

    private final Map<String, ChainExecuteInterceptor> interceptorMap = new CopyOnWriteHashMap<>();

    @Override
    public void process(String name, Interceptor interceptor) {
        if (interceptor instanceof ChainExecuteInterceptor && !contains(name)) {
            interceptorMap.put(name, (ChainExecuteInterceptor) interceptor);
        }
    }

    @Override
    public ChainExecuteInterceptor get(String name) {
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
    public Collection<ChainExecuteInterceptor> getRegisters() {
        return interceptorMap.values();
    }
}
