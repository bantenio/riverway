package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChannelSwitchComponent extends NodeSwitchComponent {
    @Override
    public String processSwitch(Node node, Slot slot) throws Exception {
        String channel = getCurrentRequestChannel();
        Map<String, String> channelLogicMapping = slot.getPropertyByType("channel.logic.mapping");
        if(!channelLogicMapping.containsKey(channel)) {
            throw new RuntimeException("未知的channel请求");
        }
        return channelLogicMapping.get(channel);
    }

    protected String getCurrentRequestChannel() {
        return "2";
    }
}
