package com.yomahub.liteflow.components;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.slot.SlotScope;

public abstract class OutResultComponent extends NodeComponent {

    public static final String PROPERTY_NAME_RESULT_NAME = "resultName";

    public static final String DEFAULT_RESULT_NAME = "result";

    public static final String PROPERTY_NAME_SCOPE = "scope";

    public static final String DEFAULT_SCOPE = SlotScope.SCOPE_PARAMETER;

    @Override
    public void process(Node node) throws Exception {
        Slot slot = getSlot();
        Object result = innerProcess(node);
        String resultName = DEFAULT_RESULT_NAME;
        if (slot.hasProperty(PROPERTY_NAME_RESULT_NAME)) {
            resultName = slot.getPropertyByType(PROPERTY_NAME_RESULT_NAME);
            if (StrUtil.isBlank(resultName)) {
                resultName = DEFAULT_RESULT_NAME;
            }
        }
        String scope = DEFAULT_SCOPE;
        if (slot.hasProperty(PROPERTY_NAME_SCOPE)) {
            scope = slot.getPropertyByType(PROPERTY_NAME_SCOPE);
            if (StrUtil.isBlank(scope)) {
                scope = DEFAULT_SCOPE;
            }
        }
        if (StrUtil.equals(scope, DEFAULT_SCOPE)) {
            slot.putParameter(resultName, result);
        } else if (StrUtil.equals(scope, SlotScope.SCOPE_VARIABLE)) {
            slot.putVariable(resultName, result);
        } else if (StrUtil.equals(scope, SlotScope.SCOPE_RESPONSE)) {
            slot.putVariable(resultName, result);
        }
    }

    public abstract Object innerProcess(Node node) throws Exception;
}
