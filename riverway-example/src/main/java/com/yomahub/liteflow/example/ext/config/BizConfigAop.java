package com.yomahub.liteflow.example.ext.config;

import cn.hutool.core.text.CharSequenceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class BizConfigAop {

    private static final Logger log = LoggerFactory.getLogger(BizConfigAop.class);

    private Map<Class<?>, Map<String, ValueRef>> classFieldValueRefMapping = new ConcurrentHashMap<>();

    private PropertiesResolver propertiesResolver;

    public BizConfigAop(PropertiesResolver propertiesResolver) {
        this.propertiesResolver = propertiesResolver;
    }

    @Pointcut("@within(com.yomahub.liteflow.example.ext.config.BizConfigIsolation) && execution(public * get*())")
    private void pointcutGetterWithBizConfigIsolation() {
    }

    @Around("pointcutGetterWithBizConfigIsolation()")
    protected Object aroundGetterWithBizConfigIsolation(ProceedingJoinPoint jp) throws Throwable {
        Object object = jp.getTarget();
        Class<?> clazz = object.getClass();
        String getterName = jp.getSignature().getName();
        String fieldName = CharSequenceUtil.getGeneralField(getterName);
        Field field = clazz.getDeclaredField(fieldName);
        ValueRef valueRef = getValueRefValue(clazz, fieldName, field);
        if (valueRef == null) {
            return jp.proceed();
        }
        return propertiesResolver.resolvePlaceholders(valueRef.value(), field.getType());
    }

    protected ValueRef getValueRefValue(Class<?> clazz, String fieldName, Field field) {
        Map<String, ValueRef> fieldValueRefMapping = classFieldValueRefMapping.computeIfAbsent(clazz, key -> new ConcurrentHashMap<>());
        return fieldValueRefMapping.computeIfAbsent(fieldName, key -> field.getAnnotation(ValueRef.class));
    }
}
