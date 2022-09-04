package com.yomahub.liteflow.example.ext.config;

import cn.hutool.core.text.CharSequenceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class BizConfigAop implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(BizConfigAop.class);

    private Map<Class<?>, Map<String, ValueRef>> classFieldValueRefMapping = new ConcurrentHashMap<>();

    private Environment environment;

    @Pointcut("@within(com.yomahub.liteflow.example.ext.config.BizConfigIsolation) && execution(public * get*())")
    private void pointcutGetterWithBizConfigIsolation() {
    }

    @Around("pointcutGetterWithBizConfigIsolation()")
    protected Object aroundGetterWithBizConfigIsolation(ProceedingJoinPoint jp) throws Throwable {
        Object object = jp.getTarget();
        Class<?> clazz = object.getClass();
        String getterName = jp.getSignature().getName();
        String fieldName = CharSequenceUtil.getGeneralField(getterName);
        ValueRef valueRef = getValueRefValue(clazz, fieldName);
        if (valueRef == null) {
            return jp.proceed();
        }
        return environment.resolveRequiredPlaceholders(valueRef.value());
    }

    protected ValueRef getValueRefValue(Class<?> clazz, String fieldName) {
        Map<String, ValueRef> fieldValueRefMapping = classFieldValueRefMapping.computeIfAbsent(clazz, key -> new ConcurrentHashMap<>());
        return fieldValueRefMapping.computeIfAbsent(fieldName, key -> getFieldValueRefValue(clazz, key));
    }

    protected ValueRef getFieldValueRefValue(Class<?> clazz, String fieldName) {
        ValueRef valueRef = null;
        String className = clazz.getName();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            valueRef = field.getAnnotation(ValueRef.class);
        } catch (NoSuchFieldException e) {
            log.error("Not found the field {} in class {}", fieldName, className, e);
        }
        return valueRef;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
