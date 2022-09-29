package com.yomahub.liteflow.property;

public class ExecutorServiceProperties {

    private String name;
    private int corePoolSize;

    private int maximumPoolSize;

    private int queueCapacity;

    private long keepAliveTime;

    private String threadName;

    public String getName() {
        return name;
    }

    public ExecutorServiceProperties setName(String name) {
        this.name = name;
        return this;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ExecutorServiceProperties setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public ExecutorServiceProperties setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public ExecutorServiceProperties setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        return this;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public ExecutorServiceProperties setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public String getThreadName() {
        return threadName;
    }

    public ExecutorServiceProperties setThreadName(String threadName) {
        this.threadName = threadName;
        return this;
    }
}
