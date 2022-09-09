package com.yomahub.liteflow.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yomahub.liteflow.builder.FlowConfigurationBuilder;
import com.yomahub.liteflow.components.QLRefValueExpressionRunner;
import com.yomahub.liteflow.components.SPELRefValueExpressionRunner;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.example.ext.flow.gyf.ExtFuncInterceptor;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.parser.ResourceELFlowParser;
import com.yomahub.liteflow.parser.ResourceGyfFlowParser;
import com.yomahub.liteflow.plugins.gyf.GyfChainBuilderSubPluginManage;
import com.yomahub.liteflow.property.ExecutorProperties;
import com.yomahub.liteflow.property.ExecutorServiceProperties;
import com.yomahub.liteflow.property.LiteFlowConfig;
import com.yomahub.liteflow.spring.ComponentScanner;
import com.yomahub.liteflow.thread.ExecutorServiceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tenio.interstellar.jackson.ObjectMapperFactory;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class LiteFlowConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperFactory.objectMapper();
    }

    @Bean
    public LiteFlowConfig liteFlowConfig(ExecutorProperties executorProperties) {
        return new LiteFlowConfig().setFlowPaths(Arrays.asList("gyf:classpath*://gyfs/main.gyf"))
                .setRefType(SPELRefValueExpressionRunner.REF_VALUE_TYPE)
                .setExecutorProperties(executorProperties);
    }

    @Bean
    public ExecutorServiceManager executorServiceManager(ExecutorProperties executorProperties) {
        ExecutorServiceManager manager = new ExecutorServiceManager();
        for (ExecutorServiceProperties serviceProperty : executorProperties.getServiceProperties()) {
            manager.build(serviceProperty);
        }
        return manager;
    }

//    @Bean
//    public ExecutorServiceManager executorServiceManager(Map<String, ExecutorService> executorServiceMap) {
//        ExecutorServiceManager manager = new ExecutorServiceManager();
//        for (Map.Entry<String, ExecutorService> stringExecutorServiceEntry : executorServiceMap.entrySet()) {
//            manager.registerExecutorService(stringExecutorServiceEntry.getKey(), stringExecutorServiceEntry.getValue());
//        }
//        return manager;
//    }

    @Bean
    public ExecutorProperties executorProperties() {
        return new ExecutorProperties().setMainExecutorServiceName("main").setServiceProperties(Arrays.asList(
                new ExecutorServiceProperties().setName("main").setCorePoolSize(10).setMaximumPoolSize(20).setQueueCapacity(100).setKeepAliveTime(60 * 1000),
                new ExecutorServiceProperties().setName("sub").setCorePoolSize(10).setMaximumPoolSize(20).setQueueCapacity(100).setKeepAliveTime(60 * 1000)
        ));
    }

    @Bean
    public FlowConfiguration flowConfiguration(LiteFlowConfig liteFlowConfig, ExecutorServiceManager executorServiceManager, Map<String, NodeComponent> ignores, ExtFuncInterceptor extFuncInterceptor) {
        ResourceGyfFlowParser.register();
        SPELRefValueExpressionRunner.registerSelf();
        QLRefValueExpressionRunner.registerSelf();
        return FlowConfigurationBuilder
                .create()
                .setExecutorServiceManager(executorServiceManager)
                .setNodeComponentMap(ignores)
                .setLiteFlowConfig(liteFlowConfig)
                .addSubManage(new GyfChainBuilderSubPluginManage())
                .registerInterceptor("extFunc", extFuncInterceptor)
                .build();
    }

    @Bean
    public ComponentScanner componentScanner() {
        return new ComponentScanner();
    }


//    @Bean
//    public FlowConfiguration flowConfiguration(LiteFlowConfig liteFlowConfig, List<NodeComponent> ignores, Map<String, ExecutorService> executorServiceMap) {
//        ResourceELFlowParser.register();
//        FlowConfiguration flowConfiguration = FlowConfigurationBuilder
//                .create()
//                .setLiteflowConfig(liteFlowConfig)
//                .build();
//        ExecutorServiceManager manager = flowConfiguration.getExecutorServiceManager();
//        for (Map.Entry<String, ExecutorService> stringExecutorServiceEntry : executorServiceMap.entrySet()) {
//            manager.registerExecutorService(stringExecutorServiceEntry.getKey(), stringExecutorServiceEntry.getValue());
//        }
//        return flowConfiguration;
//    }
}
