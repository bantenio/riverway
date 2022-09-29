package com.yomahub.liteflow.components;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringExpressionCache {

    private static Map<Integer, Expression> spelExpressionMap = new ConcurrentHashMap<>();
    private static SpelParserConfiguration config = new SpelParserConfiguration(true, true);
    private static SpelExpressionParser parser = new SpelExpressionParser(config);

    public static Expression obtainExpression(String strExpression) {
        Integer hash = strExpression.hashCode();
        Expression expression;
        if (spelExpressionMap.containsKey(hash)) {
            expression = spelExpressionMap.get(hash);
        } else {
            expression = parser.parseExpression(strExpression);
            spelExpressionMap.put(hash, expression);
        }
        return expression;
    }
}
