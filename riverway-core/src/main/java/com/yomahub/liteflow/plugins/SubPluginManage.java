package com.yomahub.liteflow.plugins;

import java.util.Collection;

public interface SubPluginManage<T extends Interceptor> {

    void process(String name, Interceptor interceptor);

    T get(String name);

    boolean contains(String name);

    void unregister(String name);

    int size();

    boolean isEmpty();

    String getName();

    <K extends Interceptor> Collection<K> getRegisters();
}
