package com.yomahub.liteflow.script;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 脚本组件类
 *
 * @author Bryan.Zhang
 * @since 2.6.0
 */
public class ScriptComponent extends NodeComponent {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object handle(Node node, Slot slot) throws Exception {
        return ScriptExecutorFactory.loadInstance().getScriptExecutor().execute(this.getCurrChainName(), getNodeId(), getSlotIndex());
    }

    public void loadScript(String script) {
        log.info("load script for component[{}]", getDisplayName());
        ScriptExecutorFactory.loadInstance().getScriptExecutor().load(getNodeId(), script);
    }
}
