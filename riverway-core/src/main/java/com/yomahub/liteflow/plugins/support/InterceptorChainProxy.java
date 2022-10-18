package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.condition.Condition;
import com.yomahub.liteflow.plugins.*;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptorChainProxy extends Chain<InterceptorChainProxy> {
    private static final Logger log = LoggerFactory.getLogger(InterceptorChainProxy.class);

    private final Chain<? extends Chain<?>> delegate;

    private final FlowConfiguration flowConfiguration;

    private final ThreadLocal<Map<Interceptor, ChainInterceptorContext>> interceptorContextThreadLocal = new ThreadLocal<>();

    public InterceptorChainProxy(Chain<? extends Chain<?>> delegate, FlowConfiguration flowConfiguration) {
        this.delegate = delegate;
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public void process(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        Slot slot = DataBus.getSlot(slotIndex);
        beforeExecuteForInterceptor(delegate, slot, flowConfiguration);
        try {
            delegate.execute(slotIndex, flowConfiguration);
            afterExecuteForInterceptor(delegate, slot, flowConfiguration);
        } catch (ChainEndException e) {
            throw e;
        } catch (Exception e) {
            this.afterExceptionForInterceptor(delegate, slot, flowConfiguration, e);
            throw e;
        } finally {
            this.afterFinallyForInterceptor(delegate, slot, flowConfiguration);
        }
    }

    public void beforeExecuteForInterceptor(Chain<? extends Chain<?>> delegate,
                                            Slot slot,
                                            FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, ChainInterceptorContext> interceptorContextMap = new HashMap<>();
            interceptorContextThreadLocal.set(interceptorContextMap);
            Collection<ChainExecuteInterceptor> interceptors = pluginManager
                    .getPluginManage(ChainExecutorPluginManage.PLUGIN_MANAGE_NAME)
                    .getRegisters();
            try {
                for (ChainExecuteInterceptor interceptor : interceptors) {
                    ChainInterceptorContext interceptorContext = new ChainInterceptorContext(flowConfiguration)
                            .setChainName(delegate.getChainName()).setFinally(false).setHasError(false);
                    interceptorContext.setContext(interceptor.initContext(interceptorContext));
                    interceptorContextMap.put(interceptor, interceptorContext);
                    interceptor.beforeProcess(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    public void afterExceptionForInterceptor(Chain<? extends Chain<?>> delegate,
                                             Slot slot,
                                             FlowConfiguration flowConfiguration,
                                             Exception e) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, ChainInterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<ChainExecuteInterceptor> interceptors = pluginManager
                    .getPluginManage(ChainExecutorPluginManage.PLUGIN_MANAGE_NAME)
                    .getRegisters();
            try {
                for (ChainExecuteInterceptor interceptor : interceptors) {
                    ChainInterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                    interceptorContext.setError(e).setHasError(true);
                    interceptor.onError(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    public void afterExecuteForInterceptor(Chain<? extends Chain<?>> delegate,
                                           Slot slot,
                                           FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, ChainInterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<ChainExecuteInterceptor> interceptors = pluginManager
                    .getPluginManage(ChainExecutorPluginManage.PLUGIN_MANAGE_NAME)
                    .getRegisters();
            try {
                for (ChainExecuteInterceptor interceptor : interceptors) {
                    ChainInterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                    interceptorContext.setHasError(false).setError(null);
                    interceptor.onSuccess(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    public void afterFinallyForInterceptor(Chain<? extends Chain<?>> delegate,
                                           Slot slot,
                                           FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, ChainInterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<ChainExecuteInterceptor> interceptors = pluginManager
                    .getPluginManage(ChainExecutorPluginManage.PLUGIN_MANAGE_NAME)
                    .getRegisters();
            try {
                for (ChainExecuteInterceptor interceptor : interceptors) {
                    ChainInterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                    interceptorContext.setFinally(true);
                    interceptor.onFinally(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
            interceptorContextMap.clear();
            interceptorContextThreadLocal.remove();
        }
    }

    @Override
    public List<Condition<? extends Condition<?>>> getConditionList() {
        return delegate.getConditionList();
    }

    @Override
    public InterceptorChainProxy setConditionList(List<Condition<? extends Condition<?>>> conditionList) {
        delegate.setConditionList(conditionList);
        return getSelf();
    }

    @Override
    public String getChainName() {
        return delegate.getChainName();
    }

    @Override
    public Chain setChainName(String chainName) {
        return delegate.setChainName(chainName);
    }

    @Override
    public void executeConditions(Integer slotIndex, List<Condition<? extends Condition<?>>> conditionList, String chainName, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeConditions(slotIndex, conditionList, chainName, flowConfiguration);
    }

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return delegate.getExecuteType();
    }

    @Override
    public String getExecuteName() {
        return delegate.getExecuteName();
    }

    @Override
    public List<Condition<? extends Condition<?>>> getPreConditionList() {
        return delegate.getPreConditionList();
    }

    @Override
    public InterceptorChainProxy setPreConditionList(List<Condition<? extends Condition<?>>> preConditionList) {
        delegate.setPreConditionList(preConditionList);
        return getSelf();
    }

    @Override
    public List<Condition<? extends Condition<?>>> getFinallyConditionList() {
        return delegate.getFinallyConditionList();
    }

    @Override
    public InterceptorChainProxy setFinallyConditionList(List<Condition<? extends Condition<?>>> finallyConditionList) {
        delegate.setFinallyConditionList(finallyConditionList);
        return getSelf();
    }

    @Override
    public boolean isAccess(Integer slotIndex) throws Throwable {
        return delegate.isAccess(slotIndex);
    }

    @Override
    public InterceptorChainProxy setCurrChainName(String currentChainName) {
        delegate.setCurrChainName(currentChainName);
        return getSelf();
    }
}
