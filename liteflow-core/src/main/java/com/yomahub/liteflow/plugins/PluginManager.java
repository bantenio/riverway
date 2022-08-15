package com.yomahub.liteflow.plugins;

import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.Collection;
import java.util.Map;

public class PluginManager {
    private Map<String, Interceptor> interceptors = new CopyOnWriteHashMap<>();

    public void register(String name, Interceptor interceptor) {
        interceptors.put(name, interceptor);
    }

    public Interceptor get(String name) {
        return interceptors.get(name);
    }

    public boolean contains(String name) {
        return interceptors.containsKey(name);
    }

    public void unregister(String name) {
        interceptors.remove(name);
    }

    public Collection<Interceptor> getRegisters() {
        return interceptors.values();
    }

    public int size() {
        return interceptors.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }
}
