package com.yomahub.liteflow.example.ext.config;

public interface PropertiesResolver {
    Object resolvePlaceholders(String text, Class<?> targetType);
}
