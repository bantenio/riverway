package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.springframework.stereotype.Component;

@Component
public class WriteTransResponseToRedisComponent extends NodeComponent {

    @Override
    public void process(Node node, Slot slot) throws Throwable {
        System.out.println("WriteTransResponseToRedisComponent print:" + slot.getParameter("transQueryResponse"));
    }
}
