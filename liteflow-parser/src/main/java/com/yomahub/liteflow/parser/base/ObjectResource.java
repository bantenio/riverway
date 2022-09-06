package com.yomahub.liteflow.parser.base;

import com.yomahub.liteflow.builder.ParseResource;

public class ObjectResource<T> extends ParseResource {
    private T object;

    public T getObject() {
        return object;
    }

    public ObjectResource<T> setObject(T object) {
        this.object = object;
        return this;
    }
}
