package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendLoanToBigDataNodeComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(SendLoanToBigDataNodeComponent.class);
    @Override
    public void process(Node node) throws Exception {
        log.info("SendLoanToBigDataNodeComponent.process: {}", node.getId());
    }
}
