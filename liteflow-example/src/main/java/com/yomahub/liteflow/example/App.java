package com.yomahub.liteflow.example;

import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.slot.DefaultContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private FlowConfiguration flowConfiguration;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LiteflowResponse liteflowResponse = flowConfiguration.getFlowExecutor().execute2Resp("chain1", "chain1", new DefaultContext());
        String result = liteflowResponse.getContextBean(DefaultContext.class).getData("result");
        log.info("result: {}", result);
    }
}
