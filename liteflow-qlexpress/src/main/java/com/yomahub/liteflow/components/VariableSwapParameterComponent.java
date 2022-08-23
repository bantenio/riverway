package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

import java.util.Map;

public class VariableSwapParameterComponent extends NodeComponent {

    public static final String PROPERTY_NAME_VAR_TO_PARAMETER = "varToParam";

    @Override
    public void process(Node node) throws Exception {
        Slot slot = getSlot();
        if (slot.hasProperty(PROPERTY_NAME_VAR_TO_PARAMETER)) {
            Map<String, Object> variableToParameterMapping = slot.getPropertyByType("varToParam");
            if (!variableToParameterMapping.isEmpty()) {
                for (Map.Entry<String, Object> entry : variableToParameterMapping.entrySet()) {
                    Object valRef = entry.getValue();
                    if (valRef instanceof String) {
                        slot.putParameter(entry.getKey(), slot.getVariable(valRef.toString()));
                    } else if (valRef instanceof ValueHandler) {
                        Object val = ((ValueHandler) valRef).getValue(slot, node);
                        slot.putParameter(entry.getKey(), val);
                    }
                }
            }
        }
    }
}
