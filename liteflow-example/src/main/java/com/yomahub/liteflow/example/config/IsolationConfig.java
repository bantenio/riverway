package com.yomahub.liteflow.example.config;

import com.yomahub.liteflow.example.ext.config.BizConfigIsolation;
import com.yomahub.liteflow.example.ext.config.ValueRef;
import org.springframework.stereotype.Component;

@Component
@BizConfigIsolation
public class IsolationConfig {

    @ValueRef("${app.config.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
