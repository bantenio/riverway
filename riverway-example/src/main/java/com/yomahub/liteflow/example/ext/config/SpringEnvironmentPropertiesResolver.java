package com.yomahub.liteflow.example.ext.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringEnvironmentPropertiesResolver implements PropertiesResolver, EnvironmentAware {

    private AbstractEnvironment environment;


    @Override
    public void setEnvironment(Environment environment) {
        if (!(environment instanceof AbstractEnvironment)) {
            throw new IllegalArgumentException("the Environment instance is not AbstractEnvironment.");
        }
        this.environment = (AbstractEnvironment) environment;
    }

    @Override
    public Object resolvePlaceholders(String text, Class<?> targetType) {
        String strVal = environment.resolvePlaceholders(text);
        return environment.getConversionService().convert(strVal, targetType);
    }
}
