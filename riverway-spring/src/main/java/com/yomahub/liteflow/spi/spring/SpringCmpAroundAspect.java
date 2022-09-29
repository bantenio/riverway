package com.yomahub.liteflow.spi.spring;

import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.spring.ComponentScanner;

/**
 * Spring环境全局组件切面实现
 * @author Bryan.Zhang
 * @since 2.6.11
 */
public class SpringCmpAroundAspect {
    public void beforeProcess(String nodeId, Slot slot) {
        if (ObjectUtil.isNotNull(ComponentScanner.cmpAroundAspect)) {
            ComponentScanner.cmpAroundAspect.beforeProcess(nodeId, slot);
        }
    }

    public void afterProcess(String nodeId, Slot slot) {
        if (ObjectUtil.isNotNull(ComponentScanner.cmpAroundAspect)) {
            ComponentScanner.cmpAroundAspect.afterProcess(nodeId, slot);
        }
    }

    public int priority() {
        return 1;
    }
}
