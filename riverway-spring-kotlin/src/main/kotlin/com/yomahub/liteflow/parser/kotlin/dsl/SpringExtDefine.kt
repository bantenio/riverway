package com.yomahub.liteflow.parser.kotlin.dsl

import com.yomahub.liteflow.components.SpringExpressionCache
import com.yomahub.liteflow.components.SPELComponentBuilder
import com.yomahub.liteflow.flow.element.Node
import com.yomahub.liteflow.flow.element.condition.NodeCondition
import org.springframework.expression.Expression

fun apply(target: String, methodName: String, vararg args: String?): NodeCondition {
    return SPELComponentBuilder.createObtainApplyExpression(
        getExpression(
            target,
            methodName,
            args.asList()
        )
    )
}

fun accept(target: String, methodName: String, vararg args: String?): NodeCondition {
    return SPELComponentBuilder.createObtainAcceptExpression(
        getExpression(
            target,
            methodName,
            args.asList()
        )
    )
}

private fun getExpression(target: String, methodName: String, args: List<String?>?): Expression {
    var params: StringBuilder? = null
    if (args != null) {
        params = StringBuilder()
        for (arg in args) {
            if (params.isNotEmpty()) {
                params.append(',')
            }
            params.append(arg)
        }
    }
    val buf: StringBuilder = StringBuilder(target)
        .append('.')
        .append(methodName)
        .append('(')
    if (params != null) {
        buf.append(params)
    }
    buf.append(')')
    return SpringExpressionCache.obtainExpression(buf.toString())
}