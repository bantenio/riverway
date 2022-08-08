package com.yomahub.liteflow.thread;

import com.yomahub.liteflow.property.ExecutorProperties;
import com.yomahub.liteflow.property.ExecutorServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceManager {
    private static final Logger log = LoggerFactory.getLogger(ExecutorServiceManager.class);

    private final Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>(16);

    private final ExecutorBuilder executorBuilder;

    public ExecutorServiceManager() {
        this(new DefaultExecutorBuilder());
    }

    public ExecutorServiceManager(ExecutorBuilder executorBuilder) {
        this.executorBuilder = executorBuilder;
    }

    public boolean contains(String key) {
        return executorServiceMap.containsKey(key);
    }

    public ExecutorService get(String key) {
        return executorServiceMap.get(key);
    }

    public Executor build(ExecutorServiceProperties executorServiceProperties) {
        String name = executorServiceProperties.getName();
        if (contains(name)) {
            throw new IllegalStateException("the '" + name + "' ExecutorService was conflict name;");
        }
        ExecutorService executorService = executorBuilder.buildExecutor(executorServiceProperties);
        registerExecutorService(name, executorService);
        return executorService;
    }

    public void registerExecutorService(String name, ExecutorService executorService) {
        executorServiceMap.put(name, executorService);}


    public void shutdownAll() {
        for (Map.Entry<String, ExecutorService> entry : executorServiceMap.entrySet()) {
            shutdownAwaitTermination(entry.getValue());
        }
    }


    /**
     * <p>
     *
     * @param pool 需要关闭的线程组.
     */
    public void shutdownAwaitTermination(ExecutorService pool) {
        shutdownAwaitTermination(pool, 60L);
    }

    /**
     * <p>
     * 关闭ExecutorService的线程管理者
     * <p>
     *
     * @param pool    需要关闭的管理者
     * @param timeout 等待时间
     */
    public void shutdownAwaitTermination(ExecutorService pool,
                                         long timeout) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    log.error("Pool did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static class DefaultExecutorBuilder implements ExecutorBuilder {
    }
}
