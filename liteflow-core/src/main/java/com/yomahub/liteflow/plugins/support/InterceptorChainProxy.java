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

public class InterceptorChainProxy extends Chain {
    private static final Logger log = LoggerFactory.getLogger(InterceptorChainProxy.class);

    private final Chain delegate;

    private final FlowConfiguration flowConfiguration;

    private final ThreadLocal<Map<Interceptor, ChainInterceptorContext>> interceptorContextThreadLocal = new ThreadLocal<>();

    public InterceptorChainProxy(Chain delegate, FlowConfiguration flowConfiguration) {
        this.delegate = delegate;
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public void execute(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
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

    public void beforeExecuteForInterceptor(Chain delegate,
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

    public void afterExceptionForInterceptor(Chain delegate,
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

    public void afterExecuteForInterceptor(Chain delegate,
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

    public void afterFinallyForInterceptor(Chain delegate,
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
    public List<Condition> getConditionList() {
        return delegate.getConditionList();
    }

    @Override
    public void setConditionList(List<Condition> conditionList) {
        delegate.setConditionList(conditionList);
    }

    @Override
    public String getChainName() {
        return delegate.getChainName();
    }

    @Override
    public void setChainName(String chainName) {
        delegate.setChainName(chainName);
    }

    @Override
    public void executeConditions(Integer slotIndex, List<Condition> conditionList, String chainName, FlowConfiguration flowConfiguration) throws Throwable {
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
    public List<Condition> getPreConditionList() {
        return delegate.getPreConditionList();
    }

    @Override
    public void setPreConditionList(List<Condition> preConditionList) {
        delegate.setPreConditionList(preConditionList);
    }

    @Override
    public List<Condition> getFinallyConditionList() {
        return delegate.getFinallyConditionList();
    }

    @Override
    public void setFinallyConditionList(List<Condition> finallyConditionList) {
        delegate.setFinallyConditionList(finallyConditionList);
    }

    @Override
    public boolean isAccess(Integer slotIndex) throws Throwable {
        return delegate.isAccess(slotIndex);
    }

    @Override
    public void setCurrChainName(String currentChainName) {
        delegate.setCurrChainName(currentChainName);
    }
}
