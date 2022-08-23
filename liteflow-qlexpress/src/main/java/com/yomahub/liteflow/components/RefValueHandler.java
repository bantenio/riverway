package com.yomahub.liteflow.components;

import cn.hutool.core.text.CharSequenceUtil;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class RefValueHandler implements ValueHandler {

    private static final Logger log = LoggerFactory.getLogger(RefValueHandler.class);

    private static final Map<String, RefValueExpressionRunner> expressionRunnerMap = new HashMap<>();

    static {
        register("ql", new QLRefValueExpressionRunner());
    }

    private String expression;

    private String type;

    public RefValueHandler(String expression, String type) {
        if (CharSequenceUtil.isBlank(expression)) {
            throw new ELParseException("expression can not empty");
        }
        this.expression = expression;
        this.type = type;
    }

    @Override
    public Object getValue(Slot slot, Node node) {
        if (!expressionRunnerMap.containsKey(type)) {
            throw new NoSuchElementException("The " + type + " expression was not support");
        }
        return expressionRunnerMap.get(type).getValue(slot, node, expression);
    }

    public static void register(String type, RefValueExpressionRunner refValueExpressionRunner) {
        expressionRunnerMap.put(type, refValueExpressionRunner);
    }
}
