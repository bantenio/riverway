package com.yomahub.liteflow.property;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExecutorProperties {
    private List<ExecutorServiceProperties> serviceProperties = new CopyOnWriteArrayList<>();

    private String mainExecutorServiceName;

    private String executorServiceBuilderClassName;

    public List<ExecutorServiceProperties> getServiceProperties() {
        return serviceProperties;
    }

    public ExecutorProperties setServiceProperties(List<ExecutorServiceProperties> serviceProperties) {
        this.serviceProperties = serviceProperties;
        return this;
    }

    public String getMainExecutorServiceName() {
        return mainExecutorServiceName;
    }

    public ExecutorProperties setMainExecutorServiceName(String mainExecutorServiceName) {
        this.mainExecutorServiceName = mainExecutorServiceName;
        return this;
    }

    public String getExecutorServiceBuilderClassName() {
        return executorServiceBuilderClassName;
    }

    public ExecutorProperties setExecutorServiceBuilderClassName(String executorServiceBuilderClassName) {
        this.executorServiceBuilderClassName = executorServiceBuilderClassName;
        return this;
    }
}
