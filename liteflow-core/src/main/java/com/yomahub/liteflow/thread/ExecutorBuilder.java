package com.yomahub.liteflow.thread;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.yomahub.liteflow.property.ExecutorServiceProperties;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 并行多线程执行器构造器接口
 *
 * @author Bryan.Zhang
 * @since 2.6.6
 */
public interface ExecutorBuilder {

    //构建默认的线程池对象
    default ExecutorService buildExecutor(ExecutorServiceProperties executorServiceProperties) {
        return TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(executorServiceProperties.getCorePoolSize(),
                executorServiceProperties.getMaximumPoolSize(),
                executorServiceProperties.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(executorServiceProperties.getQueueCapacity()),
                new ThreadFactory() {
                    private final AtomicLong number = new AtomicLong();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread newThread = Executors.defaultThreadFactory().newThread(r);
                        newThread.setName(executorServiceProperties.getThreadName() + number.getAndIncrement());
                        newThread.setDaemon(false);
                        return newThread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()));
    }
}
