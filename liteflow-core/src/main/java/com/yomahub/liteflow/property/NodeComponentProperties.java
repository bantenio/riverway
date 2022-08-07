package com.yomahub.liteflow.property;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

public class NodeComponentProperties {

    //异步线程最大等待秒数
    private Integer whenMaxWaitSeconds;
    //重试次数
    private Integer retryCount;

    // requestId 生成器
    private String requestIdGeneratorClass;

    //替补组件class路径
    private String substituteCmpClass;

    public Integer getWhenMaxWaitSeconds() {
        if (ObjectUtil.isNull(whenMaxWaitSeconds)) {
            return 15;
        } else {
            return whenMaxWaitSeconds;
        }
    }

    public void setWhenMaxWaitSeconds(Integer whenMaxWaitSeconds) {
        this.whenMaxWaitSeconds = whenMaxWaitSeconds;
    }


    public Integer getRetryCount() {
        if (ObjectUtil.isNull(retryCount) || retryCount < 0) {
            return 0;
        } else {
            return retryCount;
        }
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getRequestIdGeneratorClass() {
        if (StrUtil.isBlank(this.requestIdGeneratorClass)) {
            return "com.yomahub.liteflow.flow.id.DefaultRequestIdGenerator";
        }
        return requestIdGeneratorClass;
    }

    public void setRequestIdGeneratorClass(String requestIdGeneratorClass) {
        this.requestIdGeneratorClass = requestIdGeneratorClass;
    }


    public String getSubstituteCmpClass() {
        return substituteCmpClass;
    }

    public void setSubstituteCmpClass(String substituteCmpClass) {
        this.substituteCmpClass = substituteCmpClass;
    }
}
