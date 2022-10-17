package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.example.service.LoanService;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tenio.interstellar.context.DataObject;

@Component
public class LoanNodeComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(LoanNodeComponent.class);
    private final LoanService loanService;

    public LoanNodeComponent(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public void process(Node node, Slot slot) throws Exception {
//        DataObject name = slot.getFirstContextBean();
        DataObject name = slot.getContentBean("body");
//        DataObject name = slot.getContextBean(DataObject.class);
        log.info("request data was: {}", name);
        DataObject dataObject = slot.getContextBean(DataObject.class);
        String result = loanService.loan(node.getId());
        dataObject.put("result", result);
        slot.putVariable("result", result);
    }
}
