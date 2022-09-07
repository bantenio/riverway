package com.yomahub.liteflow.components;

import cn.hutool.core.collection.ListUtil;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

import java.util.List;

public class QLRefValueExpressionRunner implements RefValueExpressionRunner {
    public static final String REF_VALUE_TYPE = "ql";

    private ExpressRunner expressRunner = new ExpressRunner();


    public static void registerSelf() {
        RefValueHandler.register(REF_VALUE_TYPE, new QLRefValueExpressionRunner());
    }

    @Override
    public Object getValue(Slot slot, Node node, String expression) throws RuntimeException {
        List<String> errors = ListUtil.list(true);
        DefaultContext<String, Object> defaultContext = new DefaultContext<>();
        defaultContext.putAll(slot.getContextBeanMap());
        defaultContext.putAll(slot.variables());
        try {
            return expressRunner.execute(expression, defaultContext, errors, true, true);
        } catch (Exception err) {
            StringBuilder errMsgBuf = new StringBuilder();
            for (String scriptErrorMsg : errors) {
                errMsgBuf.append("\n");
                errMsgBuf.append(scriptErrorMsg);
            }
            throw new ELParseException(err);
        }
    }
}
