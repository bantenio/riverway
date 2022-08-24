package com.yomahub.liteflow.script;

import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;

/**
 * 脚本条件节点
 * @author Bryan.Zhang
 * @since 2.6.0
 */
public class ScriptSwitchComponent extends NodeSwitchComponent {

    @Override
    public String processSwitch(Node node, Slot slot) throws Exception {
        return (String)ScriptExecutorFactory.loadInstance().getScriptExecutor().execute(this.getCurrChainName(), getNodeId(), getSlotIndex());
    }

    public void loadScript(String script) {
        ScriptExecutorFactory.loadInstance().getScriptExecutor().load(getNodeId(), script);
    }
}
