package com.yomahub.liteflow.spi.spring;

import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

/**
 * spring环境LiteflowComponent注解的处理器
 * @author Bryan.Zhang
 * @since 2.6.11
 */
public class SpringLiteflowComponentSupport {
    public String getCmpName(NodeComponent nodeComponent) {
        //判断NodeComponent是否是标识了@LiteflowComponent的标注
        //如果标注了，那么要从中取到name字段
        LiteflowComponent liteflowComponent = nodeComponent.getClass().getAnnotation(LiteflowComponent.class);
        if (ObjectUtil.isNotNull(liteflowComponent)) {
            return liteflowComponent.name();
        }else{
            return null;
        }
    }

    public int priority() {
        return 1;
    }
}