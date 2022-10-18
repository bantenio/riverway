package com.yomahub.liteflow.components.ext;

import cn.hutool.core.text.CharSequenceUtil;
import com.yomahub.liteflow.components.ConstantValueHandler;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAround;
import com.yomahub.liteflow.slot.Slot;

public class StringFormatNodeAround implements NodeAround {

    private final String message;

    private final Object[] args;

    private final String parameterName;

    public StringFormatNodeAround(String message, Object[] args, String parameterName) {
        this.message = message;
        this.args = args;
        this.parameterName = parameterName;
    }

    @Override
    public void before(NodeCondition nodeCondition, Node node, Slot slot) throws Throwable {
        Object[] tmpArgs = new Object[args.length];
        for (int idx = 0; idx < args.length; idx++) {
            Object arg = args[idx];
            if (arg instanceof ValueHandler) {
                tmpArgs[idx] = ((ValueHandler) arg).getValue(slot, node);
            } else if (arg instanceof String) {
                tmpArgs[idx] = slot.getVariable((String) arg);
            } else {
                tmpArgs[idx] = new ConstantValueHandler(arg);
            }
        }
        nodeCondition.addSwapHandler(parameterName, new ConstantValueHandler(CharSequenceUtil.format(message, tmpArgs)));
    }
}
