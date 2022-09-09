package com.yomahub.liteflow.parser.dsl.define

import cn.hutool.core.util.StrUtil
import com.yomahub.liteflow.builder.prop.NodePropBean
import com.yomahub.liteflow.core.NodeComponent

class NodeSpec {
    NodePropBean nodePropBean

    void id(String id) {
        nodePropBean.setId(id)
    }

    void clazz(String clazz) {
        nodePropBean.setClazz(clazz)
    }

    void clazz(Class<? extends NodeComponent> nodeClass) {
        clazz(nodeClass.getName())
    }

    void idWithClazz(Class<? extends NodeComponent> nodeClass) {
        clazz(nodeClass)
        id(StrUtil.lowerFirst(nodeClass.getSimpleName()))
    }

    void type(String type) {
        nodePropBean.setType(type)
    }

    void name(String name) {
        nodePropBean.setName(name)
    }
}