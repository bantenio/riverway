package com.yomahub.liteflow.parser.groovy.dsl

import com.yomahub.liteflow.components.SPELComponentBuilder
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.NodeCondition
import groovy.transform.TypeChecked

@TypeChecked
class SpringExtDefine {
    static NodeCondition apply(String target, String methodName, String... args) {
        List<String> argList = null
        if(args != null || args.length > 0) {
            argList = new ArrayList<>(args.length)
            for (final def arg in args) {
                argList.add(arg)
            }
        }
        return SPELComponentBuilder.createObtainApplyExpression(
                SPELUtil.getExpression(target, methodName, argList)
        )
    }

    static NodeCondition accept( String target, String methodName, String... args) {
        List<String> argList = null
        if(args != null || args.length > 0) {
            argList = new ArrayList<>(args.length)
            for (final def arg in args) {
                argList.add(arg)
            }
        }
        return SPELComponentBuilder.createObtainAcceptExpression(
                SPELUtil.getExpression(target, methodName, argList)
        )
    }
}
