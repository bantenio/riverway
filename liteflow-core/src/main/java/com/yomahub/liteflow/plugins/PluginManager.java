package com.yomahub.liteflow.plugins;

import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.Collection;
import java.util.Map;

public class PluginManager {
    private Map<String, Interceptor> interceptors = new CopyOnWriteHashMap<>();

    private Map<String, ChainExecuteInterceptor> chainExecuteInterceptorMap = new CopyOnWriteHashMap<>();

    private Map<String, NodeComponentExecuteInterceptor> nodeComponentExecuteInterceptorMap = new CopyOnWriteHashMap<>();

    public void register(String name, Interceptor interceptor) {
        interceptors.put(name, interceptor);
        if (interceptor instanceof ChainExecuteInterceptor) {
            chainExecuteInterceptorMap.put(name, (ChainExecuteInterceptor) interceptor);
        } else if (interceptor instanceof NodeComponentExecuteInterceptor) {
            nodeComponentExecuteInterceptorMap.put(name, (NodeComponentExecuteInterceptor) interceptor);
        }
    }

    public Interceptor get(String name) {
        return interceptors.get(name);
    }

    public boolean contains(String name) {
        return interceptors.containsKey(name);
    }

    public void unregister(String name) {
        Interceptor interceptor = interceptors.remove(name);
        if (interceptor == null) {
            return;
        }
        if (interceptor instanceof ChainExecuteInterceptor) {
            chainExecuteInterceptorMap.remove(name);
        } else if (interceptor instanceof NodeComponentExecuteInterceptor) {
            nodeComponentExecuteInterceptorMap.remove(name);
        }
    }

    public Collection<ChainExecuteInterceptor> getChainRegisters() {
        return chainExecuteInterceptorMap.values();
    }

    public Collection<NodeComponentExecuteInterceptor> getNodeComponentRegisters() {
        return nodeComponentExecuteInterceptorMap.values();
    }

    public int size() {
        return interceptors.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int chainSize() {
        return chainExecuteInterceptorMap.size();
    }

    public boolean chainIsEmpty() {
        return this.chainSize() == 0;
    }

    public int nodeComponentSize() {
        return nodeComponentExecuteInterceptorMap.size();
    }

    public boolean nodeComponentIsEmpty() {
        return this.nodeComponentSize() == 0;
    }
}
