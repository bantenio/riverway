package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.example.service.LoanService;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DefaultContext;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoanNodeComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(LoanNodeComponent.class);
    private final LoanService loanService;

    public LoanNodeComponent(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public void process(Node node) throws Exception {
        Slot slot = getSlot();
        String name = slot.getRequestData();
        log.info("request data was: {}", name);
        DefaultContext defaultContext = slot.getContextBean(DefaultContext.class);
        String result = loanService.loan(node.getId());
        defaultContext.setData("result", result);
    }
}
