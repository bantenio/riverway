package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendLoanToBigDataNodeComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(SendLoanToBigDataNodeComponent.class);
    @Override
    public void process(Node node) throws Exception {
        Slot slot = getSlot();
        log.info("SendLoanToBigDataNodeComponent.process: {}", node.getId());
        log.info("SendLoanToBigDataNodeComponent.process param1: {}, param2: {}", slot.getParameter("param1"), slot.getParameter("param2"));

    }
}
