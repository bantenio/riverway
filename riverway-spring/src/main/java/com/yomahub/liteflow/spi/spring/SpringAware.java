package com.yomahub.liteflow.spi.spring;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 基于代码形式的spring上下文工具类
 * @author Bryan.Zhang
 */
public class SpringAware implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public SpringAware() {
    }

    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public <T> T getBean(String name) {
        try{
            T t = (T) applicationContext.getBean(name);
            return t;
        }catch (Exception e){
            return null;
        }
    }

    public <T> T getBean(Class<T> clazz) {
        try{
            T t = applicationContext.getBean(clazz);
            return t;
        }catch (Exception e){
            return null;
        }
    }

    public <T> T registerBean(String beanName, Class<T> c) {
        try{
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
            BeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClassName(c.getName());
            beanFactory.setAllowBeanDefinitionOverriding(true);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
            return getBean(beanName);
        }catch (Exception e){
            return ReflectUtil.newInstance(c);
        }
    }

    public <T> T registerBean(Class<T> c) {
        return registerBean(c.getName(), c);
    }

    public <T> T registerBean(String beanName, Object bean) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.registerSingleton(beanName,bean);
        return (T) configurableApplicationContext.getBean(beanName);
    }

    public <T> T registerOrGet(String beanName, Class<T> clazz) {
        if (ObjectUtil.isNull(applicationContext)){
            return null;
        }
        T t = getBean(clazz);
        if (ObjectUtil.isNull(t)) {
            t = registerBean(beanName, clazz);
        }
        return t;
    }

    public int priority() {
        return 1;
    }
}
