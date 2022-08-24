package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PreProcessNodeComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(PreProcessNodeComponent.class);
    @Override
    public void process(Node node) throws Exception {
        log.info("短信发送前数据处理");
    }
}
