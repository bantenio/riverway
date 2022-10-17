package com.yomahub.liteflow.example.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.example.config.IsolationConfig;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PreProcessNodeComponent extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(PreProcessNodeComponent.class);
    @Autowired
    private IsolationConfig config;

    @Override
    public void process(Node node, Slot slot) throws Exception {
        System.out.println(config.getPort());
        log.info("短信发送前数据处理: {}", config.getUrl());
    }
}
