package com.yomahub.liteflow.example.controller;

import com.yomahub.liteflow.example.context.DataObject;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                         HttpServletResponse response,
                         HttpHeaders headers) throws Exception {
        LiteflowResponse liteflowResponse = flowConfiguration.getFlowExecutor().execute2Resp(chainId, body, request, response, headers);
        boolean isSuccessfully = liteflowResponse.isSuccess();
        if (!isSuccessfully) {
            throw liteflowResponse.getCause();
        }
        return body.getDataObject("result");
    }
}
