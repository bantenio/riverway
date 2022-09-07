package com.yomahub.liteflow.builder.gyf.prop

import com.yomahub.liteflow.builder.prop.ChainPropBean
import com.yomahub.liteflow.enums.ConditionTypeEnum

class GyfChainPropBean {
    private String chainPath

    private ChainPropBean chainPropBean

    String getChainPath() {
        return chainPath
    }

    void setChainPath(String chainPath) {
        this.chainPath = chainPath
    }

    void chainPropBean(ChainPropBean chainPropBean) {
        this.chainPropBean = chainPropBean
    }

    String getCondValueStr() {
        return chainPropBean.getCondValueStr();
    }

    GyfChainPropBean setCondValueStr(String condValueStr) {
        chainPropBean.setCondValueStr(condValueStr)
        return this;
    }

    String getGroup() {
        return chainPropBean.getGroup();
    }

    GyfChainPropBean setGroup(String group) {
        chainPropBean.setGroup(group)
        return this;
    }

    String getErrorResume() {
        return chainPropBean.getErrorResume();
    }

    GyfChainPropBean setErrorResume(String errorResume) {
        chainPropBean.setErrorResume(errorResume)
        return this;
    }

    String getAny() {
        return chainPropBean.getAny();
    }

    GyfChainPropBean setAny(String any) {
        chainPropBean.setAny(any)
        return this;
    }

    String getThreadExecutorName() {
        return chainPropBean.getThreadExecutorName();
    }

    GyfChainPropBean setThreadExecutorName(String threadExecutorName) {
        chainPropBean.setThreadExecutorName(threadExecutorName)
        return this;
    }

    ConditionTypeEnum getConditionType() {
        return chainPropBean.getConditionType();
    }

    GyfChainPropBean setConditionType(ConditionTypeEnum conditionType) {
        chainPropBean.setConditionType(conditionType)
        return this;
    }

    String getChainName() {
        return chainPropBean.chainName;
    }

    GyfChainPropBean setChainName(String chainName) {
        chainPropBean.chainName = chainName
        return this;
    }
}
