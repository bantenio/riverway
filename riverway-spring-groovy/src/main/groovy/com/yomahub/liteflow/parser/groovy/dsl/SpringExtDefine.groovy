package com.yomahub.liteflow.parser.groovy.dsl

import com.yomahub.liteflow.components.SPELComponentBuilder
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.NodeCondition
import groovy.transform.TypeChecked

@TypeChecked
class SpringExtDefine {
    static NodeCondition apply(String target, String methodName, String... args) {
        return SPELComponentBuilder.createObtainApplyExpression(
                SPELUtil.getExpression(target, methodName, Arrays.asList(args))
        )
    }

    static NodeCondition accept( String target, String methodName, String... args) {
        return SPELComponentBuilder.createObtainAcceptExpression(
                SPELUtil.getExpression(target, methodName, Arrays.asList(args))
        )
    }
}
