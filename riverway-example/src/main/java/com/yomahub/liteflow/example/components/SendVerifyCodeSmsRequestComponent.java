package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendVerifyCodeSmsRequestComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(SendVerifyCodeSmsRequestComponent.class);

    @Override
    public void process(Node node, Slot slot) throws Exception {
        Integer smsLen = slot.getPropertyByType("sms.len");
        String mobile = slot.getParameterByType("mobile");
        sendVerifyCodeToMobile(smsLen, mobile);
    }

    protected void sendVerifyCodeToMobile(Integer smsLen, String mobile) {
        log.info("send verify code len was : {}", smsLen);
        log.info("send to mobile was： {}", mobile);
    }
}
