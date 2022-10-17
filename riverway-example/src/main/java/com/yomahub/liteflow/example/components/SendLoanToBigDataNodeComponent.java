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
    public void process(Node node, Slot slot) throws Exception {
        log.info("SendLoanToBigDataNodeComponent.process variable result: {}", slot.getVariable("result"));
        log.info("SendLoanToBigDataNodeComponent.process param1: {}, param2: {}", slot.getProperty("param1"), slot.getProperty("param2"));
    }
}
