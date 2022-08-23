package com.yomahub.liteflow.example.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import com.yomahub.liteflow.example.context.DataObject;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/flow")
public class FlowController {

    private final FlowConfiguration flowConfiguration;

    public FlowController(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
    }


    @PostMapping("/do/{chain_id}")
    public Object doFlow(@PathVariable("chain_id") String chainId,
                         @RequestBody DataObject body,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        MapBuilder<String, Object> builder = MapBuilder.create(true);
        Map<String, Object> map = builder.put("body", body).put("request", request).put("response", response).build();
        LiteflowResponse liteflowResponse = flowConfiguration.getFlowExecutor().execute2Resp(chainId, map);
        boolean isSuccessfully = liteflowResponse.isSuccess();
        if (!isSuccessfully) {
            throw liteflowResponse.getCause();
        }
        return body.getValue("result");
    }
}
