/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.spring;

import com.yomahub.liteflow.aop.ICmpAroundAspect;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.util.LiteFlowProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 组件扫描类，只要是NodeComponent的实现类，都可以被这个扫描器扫到
 *
 * @author Bryan.Zhang
 */
public class ComponentScanner implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentScanner.class);

    public static ICmpAroundAspect cmpAroundAspect;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();

        //判断是不是声明式组件
        //如果是，就缓存到类属性的map中
        if (LiteFlowProxyUtil.isDeclareCmp(bean.getClass())) {
            LOG.info("proxy component[{}] has been found", beanName);
            NodeComponent nodeComponent = LiteFlowProxyUtil.proxy2NodeComponent(bean, beanName);
            return nodeComponent;
        }

        // 组件的扫描发现，扫到之后缓存到类属性map中
        if (NodeComponent.class.isAssignableFrom(clazz)) {
            LOG.info("component[{}] has been found", beanName);
            NodeComponent nodeComponent = (NodeComponent) bean;
            initNodeComponent(nodeComponent, beanName);
            return nodeComponent;
        }

        // 组件Aop的实现类加载
        if (ICmpAroundAspect.class.isAssignableFrom(clazz)) {
            LOG.info("component aspect implement[{}] has been found", beanName);
            cmpAroundAspect = (ICmpAroundAspect) bean;
            return cmpAroundAspect;
        }

        return bean;
    }

    protected void initNodeComponent(NodeComponent nodeComponent, String beanName) {
        if (nodeComponent instanceof NodeSwitchComponent) {
            nodeComponent.setType(NodeTypeEnum.SWITCH);
        } else {
            nodeComponent.setType(NodeTypeEnum.COMMON);
        }
        nodeComponent.setNodeId(beanName);
    }
}

