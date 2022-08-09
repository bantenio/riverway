package com.yomahub.liteflow.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yomahub.liteflow.example.context.DataArray;
import com.yomahub.liteflow.example.context.DataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ObjectMapper objectMapper;
    @PostMapping("/hello")
    public Object test(@RequestBody DataObject testData) throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(testData));
        return new DataArray().add(new DataObject().put("name", "test"));
    }
}
