package com.yomahub.liteflow.parser.dsl

import com.yomahub.liteflow.core.NodeComponent

import java.util.function.Function

interface NodeDefines {

    void define(Function<Class<? extends NodeComponent>, Node> defineNode)
}