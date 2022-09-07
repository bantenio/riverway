package com.yomahub.liteflow.example.controller;

import cn.hutool.core.map.MapBuilder;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.tenio.interstellar.context.DataObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/flow")
public class FlowController {
    private static final Logger log = LoggerFactory.getLogger(FlowController.class);

    private final FlowConfiguration flowConfiguration;

    public FlowController(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
    }


    @PostMapping("/do/{chain_id}")
    public Object doFlow(@PathVariable("chain_id") String chainId,
                         @RequestBody DataObject body,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Throwable {
        MapBuilder<String, Object> builder = MapBuilder.create(true);
        Map<String, Object> map = builder.put("body", body).put("request", request).put("response", response).build();
        LiteflowResponse liteflowResponse = flowConfiguration.getFlowExecutor().execute2Resp(chainId, map);
        boolean isSuccessfully = liteflowResponse.isSuccess();
        if (!isSuccessfully) {
            Throwable throwable = liteflowResponse.getCause();
            log.error("do chain {} on error", chainId, throwable);
            throw throwable;
        }
        return body.getValue("result");
    }
}
