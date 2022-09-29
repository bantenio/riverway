package com.yomahub.liteflow.parser.groovy.dsl

import com.yomahub.liteflow.components.SpringExpressionCache
import org.springframework.expression.Expression

class SPELUtil {
    static Expression getExpression(String target, String methodName, List<String> args) {
        StringBuilder params
        if (args != null) {
            params = new StringBuilder()
            for (arg in args) {
                if (params.size() == 0 && args.size() > 1) {
                    params.append(',')
                }
                params.append(arg)
            }
        }
        def buf = new StringBuilder(target)
                .append('.')
                .append(methodName)
                .append('(')
        if (params != null) {
            buf.append(params)
        }
        buf.append(')')
        return SpringExpressionCache.obtainExpression(buf.toString())
    }
}
