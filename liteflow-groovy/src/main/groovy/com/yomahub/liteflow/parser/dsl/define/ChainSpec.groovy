package com.yomahub.liteflow.parser.dsl.define

import com.yomahub.liteflow.builder.gyf.prop.GyfChainPropBean
import com.yomahub.liteflow.enums.ConditionTypeEnum

class ChainSpec {
    GyfChainPropBean gyfChainPropBean

    void chainPath(String path) {
        gyfChainPropBean.setChainPath(path)
    }

    void chainName(String name) {
        gyfChainPropBean.setChainName(name)
    }

    void group(String group) {
        gyfChainPropBean.setGroup(group)
    }

    void conditionType(ConditionTypeEnum conditionType) {
        gyfChainPropBean.setConditionType(conditionType)
    }

    void errorResume(String errorResume) {
        gyfChainPropBean.setErrorResume(errorResume)
    }

    void threadExecutorName(String threadExecutorName) {
        gyfChainPropBean.setThreadExecutorName(threadExecutorName)
    }

    void any(String any) {
        gyfChainPropBean.setAny(any)
    }

    GyfChainPropBean gyfChainPropBean() {
        return gyfChainPropBean
    }
}
