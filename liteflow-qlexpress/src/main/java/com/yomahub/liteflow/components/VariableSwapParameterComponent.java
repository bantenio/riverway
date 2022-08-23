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
            Map<String, String> variableToParameterMapping = slot.getPropertyByType("varToParam");
            if (!variableToParameterMapping.isEmpty()) {
                for (Map.Entry<String, String> entry : variableToParameterMapping.entrySet()) {
                    slot.putParameter(entry.getKey(), slot.getVariable(entry.getValue()));
                }
            }
        }
    }
}
