package com.yomahub.liteflow.plugins;

import com.yomahub.liteflow.util.CopyOnWriteHashMap;

import java.util.*;

public class PluginManager {
    private Map<String, Interceptor> interceptors = new CopyOnWriteHashMap<>();

    private Map<String, SubPluginManage<? extends Interceptor>> subPluginManages = new HashMap<>();

    public PluginManager registerPluginManage(SubPluginManage<? extends Interceptor> subPluginManage) {
        subPluginManages.put(subPluginManage.getName(), subPluginManage);
        return this;
    }

    public SubPluginManage<? extends Interceptor> getPluginManage(String subManageName) {
        return subPluginManages.get(subManageName);
    }

    public void register(String name, Interceptor interceptor) {
        interceptors.put(name, interceptor);
        if (subPluginManages.isEmpty()) {
            return;
        }
        for (SubPluginManage<? extends Interceptor> subPluginManage : subPluginManages.values()) {
            subPluginManage.process(name, interceptor);
        }
    }

    public void unregister(String name) {
        Interceptor interceptor = interceptors.remove(name);
        if (interceptor == null) {
            return;
        }
        if (subPluginManages.isEmpty()) {
            return;
        }
        for (SubPluginManage<? extends Interceptor> subPluginManage : subPluginManages.values()) {
            subPluginManage.unregister(name);
        }
    }

    public int size() {
        return interceptors.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }
}
