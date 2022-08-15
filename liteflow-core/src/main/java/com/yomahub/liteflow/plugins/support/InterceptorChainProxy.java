package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.plugins.ChainExecuteInterceptor;
import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.InterceptorContext;
import com.yomahub.liteflow.plugins.PluginManager;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InterceptorChainProxy extends Chain {
    private static final Logger log = LoggerFactory.getLogger(InterceptorChainProxy.class);
    private final Chain delegate;

    private final FlowConfiguration flowConfiguration;

    private final ThreadLocal<Map<Interceptor, InterceptorContext>> interceptorContextThreadLocal = new ThreadLocal<>();

    public InterceptorChainProxy(Chain delegate, FlowConfiguration flowConfiguration) {
        this.delegate = delegate;
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public void executePre(Integer slotIndex) throws Exception {
        Slot slot = DataBus.getSlot(slotIndex);
        beforeExecuteForInterceptor(delegate, slot, flowConfiguration);
        delegate.executePre(slotIndex);
    }

    public void beforeExecuteForInterceptor(Chain delegate,
                                            Slot slot,
                                            FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, InterceptorContext> interceptorContextMap = new HashMap<>();
            interceptorContextThreadLocal.set(interceptorContextMap);
            Collection<Interceptor> interceptors = pluginManager.getRegisters();
            try {
                for (Interceptor interceptor : interceptors) {
                    if (interceptor instanceof ChainExecuteInterceptor) {
                        InterceptorContext interceptorContext = new InterceptorContext()
                                .setChainName(delegate.getChainName()).setFinally(false).setHasError(false);
                        interceptorContext.setContext(interceptor.initContext(interceptorContext));
                        interceptorContextMap.put(interceptor, interceptorContext);
                        ((ChainExecuteInterceptor) interceptor).beforeProcess(interceptorContext);
                    }
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    @Override
    public void executeException(Integer slotIndex, Exception e) throws Exception {
        delegate.executeException(slotIndex, e);
        Slot slot = DataBus.getSlot(slotIndex);
        try {
            afterExceptionForInterceptor(delegate, slot, flowConfiguration, e);
        } catch (Exception ex) {
            log.error("afterExceptionForInterceptor on error", ex);
        }
    }

    public void afterExceptionForInterceptor(Chain delegate,
                                             Slot slot,
                                             FlowConfiguration flowConfiguration,
                                             Exception e) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, InterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<Interceptor> interceptors = pluginManager.getRegisters();
            try {
                for (Interceptor interceptor : interceptors) {
                    if (interceptor instanceof ChainExecuteInterceptor) {
                        InterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                        interceptorContext.setError(e).setHasError(true);
                        ((ChainExecuteInterceptor) interceptor).onError(interceptorContext);
                    }
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    @Override
    public void executeAfter(Integer slotIndex) throws Exception {
        delegate.executeAfter(slotIndex);
        Slot slot = DataBus.getSlot(slotIndex);
        try {
            afterExecuteForInterceptor(delegate, slot, flowConfiguration);
        } catch (Exception ex) {
            log.error("afterExecuteForInterceptor on error", ex);
        }
    }

    public void afterExecuteForInterceptor(Chain delegate,
                                           Slot slot,
                                           FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, InterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<Interceptor> interceptors = pluginManager.getRegisters();
            try {
                for (Interceptor interceptor : interceptors) {
                    if (interceptor instanceof ChainExecuteInterceptor) {
                        InterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                        interceptorContext.setHasError(false).setError(null);
                        ((ChainExecuteInterceptor) interceptor).onSuccess(interceptorContext);
                    }
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    @Override
    public void executeFinally(Integer slotIndex) throws Exception {
        delegate.executeFinally(slotIndex);
        Slot slot = DataBus.getSlot(slotIndex);
        try {
            afterFinallyForInterceptor(delegate, slot, flowConfiguration);
        } catch (Exception ex) {
            log.error("executeFinally on error", ex);
        }
    }

    public void afterFinallyForInterceptor(Chain delegate,
                                           Slot slot,
                                           FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, InterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<Interceptor> interceptors = pluginManager.getRegisters();
            try {
                for (Interceptor interceptor : interceptors) {
                    if (interceptor instanceof ChainExecuteInterceptor) {
                        InterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                        interceptorContext.setFinally(true);
                        ((ChainExecuteInterceptor) interceptor).onFinally(interceptorContext);
                    }
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
            interceptorContextMap.clear();
            interceptorContextThreadLocal.remove();
        }
    }
}
