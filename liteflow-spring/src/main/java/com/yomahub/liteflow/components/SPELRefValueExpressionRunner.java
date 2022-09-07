package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.tenio.interstellar.context.spring.DataObjectAccessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SPELRefValueExpressionRunner implements RefValueExpressionRunner {
    public static final String REF_VALUE_TYPE = "spel";
    private Map<Integer, Expression> spelExpressionMap = new ConcurrentHashMap<>();
    private SpelParserConfiguration config = new SpelParserConfiguration(true, true);
    private SpelExpressionParser parser = new SpelExpressionParser(config);

    @Override
    public Object getValue(Slot slot, Node node, String strExpression) throws RuntimeException {
        Integer hash = strExpression.hashCode();
        Expression expression;
        if (spelExpressionMap.containsKey(hash)) {
            expression = spelExpressionMap.get(hash);
        } else {
            expression = parser.parseExpression(strExpression);
            spelExpressionMap.put(hash, expression);
        }
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.addPropertyAccessor(new MapAccessor());
        evaluationContext.addPropertyAccessor(new DataObjectAccessor());
        evaluationContext.setVariables(slot.getContextBeanMap());
        evaluationContext.setVariables(slot.variables());
        return expression.getValue(evaluationContext);
    }

    public static void registerSelf() {
        RefValueHandler.register(REF_VALUE_TYPE, new SPELRefValueExpressionRunner());
    }
}
