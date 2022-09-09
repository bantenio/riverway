package com.yomahub.liteflow.example.ext.flow.gyf;

import com.yomahub.liteflow.builder.LiteFlowParseException;
import com.yomahub.liteflow.example.components.SPELApplyComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import org.codehaus.groovy.runtime.MethodClosure;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GyfApplyClosure extends MethodClosure {
    private Map<Integer, Expression> spelExpressionMap = new ConcurrentHashMap<>();
    private SpelParserConfiguration config = new SpelParserConfiguration(true, true);
    private SpelExpressionParser parser = new SpelExpressionParser(config);

    private ApplicationContext applicationContext;

    public GyfApplyClosure(Class<?> clazz, String methodName, ApplicationContext applicationContext) {
        super(clazz, methodName);
        this.applicationContext = applicationContext;
    }

    @Override
    public Object call(Object... args) {
        if (args.length < 2) {
            throw new LiteFlowParseException("parameter is wrong");
        }
        Object var = args[0];
        if (!(var instanceof String)) {
            throw new LiteFlowParseException("the index 0 parameter must be String");
        }
        String beanName = (String) var;
        var = args[1];
        if (!(var instanceof String)) {
            throw new LiteFlowParseException("the index 1 parameter must be String");
        }
        String methodName = (String) var;
        StringBuilder params = null;
        if (args.length > 2) {
            params = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                var = args[i];
                if (!(var instanceof String)) {
                    throw new LiteFlowParseException("the index " + i + " parameter must be String");
                }
                if (params.length() != 0) {
                    params.append(',');
                }
                params.append(var);
            }
        }
        StringBuilder buf = new StringBuilder(beanName)
                .append('.')
                .append(methodName)
                .append('(');
        if (params != null) {
            buf.append(params);
        }
        buf.append(')');
        Expression expression = obtainExpression(buf.toString());

        SPELApplyComponent spelApplyComponent = new SPELApplyComponent(applicationContext);
        Node node = new Node();
        node.setInstance(spelApplyComponent);
        node.setName("spelApplyComponent");
        NodeCondition nodeCondition = new NodeCondition(node);
        nodeCondition.addProperty("expression", expression);

        return nodeCondition;
    }

    protected Expression obtainExpression(String strExpression) {
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
