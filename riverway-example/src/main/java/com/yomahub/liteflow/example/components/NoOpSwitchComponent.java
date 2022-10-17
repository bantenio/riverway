package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.springframework.stereotype.Component;

@Component
public class NoOpSwitchComponent extends NodeSwitchComponent {
    @Override
    public String processSwitch(Node node, Slot slot) throws Exception {
        return "loanProductTypeChain";
    }
}
