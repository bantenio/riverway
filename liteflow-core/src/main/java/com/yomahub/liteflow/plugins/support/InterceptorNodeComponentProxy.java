package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.NodeComponentExecuteInterceptor;
import com.yomahub.liteflow.plugins.NodeComponentInterceptorContext;
import com.yomahub.liteflow.plugins.PluginManager;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InterceptorNodeComponentProxy extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(InterceptorNodeComponentProxy.class);
    private final NodeComponent delegate;

    private final FlowConfiguration flowConfiguration;

    private final ThreadLocal<Map<Interceptor, NodeComponentInterceptorContext>> interceptorContextThreadLocal = new ThreadLocal<>();

    public InterceptorNodeComponentProxy(NodeComponent delegate, FlowConfiguration flowConfiguration) {
        this.delegate = delegate;
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public void execute(Node node, boolean isRetry) throws Exception {
        Slot slot = delegate.getSlot();
        String chainName = slot.getChainName();
        beforeExecuteForInterceptor(chainName, node, delegate, slot, flowConfiguration, isRetry);
        try {
            delegate.execute(node, isRetry);
            afterExecuteForInterceptor(chainName, node, delegate, slot, flowConfiguration, slot.getLastStep());
        } catch (ChainEndException e) {
            throw e;
        } catch (Exception e) {
            this.afterExceptionForInterceptor(chainName, node, delegate, slot, flowConfiguration, e, slot.getLastStep());
            throw e;
        } finally {
            this.afterFinallyForInterceptor(chainName, node, delegate, slot, flowConfiguration);
        }
    }


    public void beforeExecuteForInterceptor(String chainName,
                                            Node node,
                                            NodeComponent delegate,
                                            Slot slot,
                                            FlowConfiguration flowConfiguration,
                                            boolean isRetry) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, NodeComponentInterceptorContext> interceptorContextMap = new HashMap<>();
            interceptorContextThreadLocal.set(interceptorContextMap);
            Collection<NodeComponentExecuteInterceptor> interceptors = pluginManager.getNodeComponentRegisters();
            try {
                for (NodeComponentExecuteInterceptor interceptor : interceptors) {
                    NodeComponentInterceptorContext interceptorContext =
                            (NodeComponentInterceptorContext) new NodeComponentInterceptorContext()
                                    .setNode(node)
                                    .setRetry(isRetry)
                                    .setNodeComponent(delegate)
                                    .setChainName(chainName)
                                    .setFinally(false)
                                    .setHasError(false);
                    interceptorContext.setContext(interceptor.initContext(interceptorContext));
                    interceptorContextMap.put(interceptor, interceptorContext);
                    interceptor.beforeProcess(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    public void afterExceptionForInterceptor(String chainName,
                                             Node node,
                                             NodeComponent delegate,
                                             Slot slot,
                                             FlowConfiguration flowConfiguration,
                                             Exception e,
                                             CmpStep cmpStep) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, NodeComponentInterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<NodeComponentExecuteInterceptor> interceptors = pluginManager.getNodeComponentRegisters();
            try {
                for (NodeComponentExecuteInterceptor interceptor : interceptors) {
                    NodeComponentInterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                    interceptorContext.setCmpStep(cmpStep).setError(e).setHasError(true);
                    interceptor.onError(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    public void afterExecuteForInterceptor(String chainName,
                                           Node node,
                                           NodeComponent delegate,
                                           Slot slot,
                                           FlowConfiguration flowConfiguration,
                                           CmpStep cmpStep) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, NodeComponentInterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<NodeComponentExecuteInterceptor> interceptors = pluginManager.getNodeComponentRegisters();
            try {
                for (NodeComponentExecuteInterceptor interceptor : interceptors) {
                    NodeComponentInterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
                    interceptorContext.setCmpStep(cmpStep).setHasError(false).setError(null);
                    interceptor.onSuccess(interceptorContext);
                }
            } catch (Exception ex) {
                log.error("beforeExecuteForInterceptor on error", ex);
            }
        }
    }

    public void afterFinallyForInterceptor(String chainName,
                                           Node node,
                                           NodeComponent delegate,
                                           Slot slot,
                                           FlowConfiguration flowConfiguration) throws Exception {
        PluginManager pluginManager = flowConfiguration.getPluginManager();
        if (pluginManager != null && !pluginManager.isEmpty()) {
            Map<Interceptor, NodeComponentInterceptorContext> interceptorContextMap = interceptorContextThreadLocal.get();
            Collection<NodeComponentExecuteInterceptor> interceptors = pluginManager.getNodeComponentRegisters();
            try {
                for (NodeComponentExecuteInterceptor interceptor : interceptors) {
                    NodeComponentInterceptorContext interceptorContext = interceptorContextMap.get(interceptor);
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
    public void process(Node node) throws Exception {
        delegate.process(node);
    }

    @Override
    public <T> void beforeProcess(String nodeId, Slot slot) {
        delegate.beforeProcess(nodeId, slot);
    }

    @Override
    public void onSuccess(Node node) throws Exception {
        delegate.onSuccess(node);
    }

    @Override
    public void onError(Node node) throws Exception {
        delegate.onError(node);
    }

    @Override
    public <T> void afterProcess(String nodeId, Slot slot) {
        delegate.afterProcess(nodeId, slot);
    }

    @Override
    public boolean isAccess() {
        return delegate.isAccess();
    }

    @Override
    public boolean isContinueOnError() {
        return delegate.isContinueOnError();
    }

    @Override
    public boolean isEnd() {
        return delegate.isEnd();
    }

    @Override
    public void setIsEnd(boolean isEnd) {
        delegate.setIsEnd(isEnd);
    }

    @Override
    public void removeIsEnd() {
        delegate.removeIsEnd();
    }

    @Override
    public NodeComponent setSlotIndex(Integer slotIndex) {
        return delegate.setSlotIndex(slotIndex);
    }

    @Override
    public Integer getSlotIndex() {
        return delegate.getSlotIndex();
    }

    @Override
    public void removeSlotIndex() {
        delegate.removeSlotIndex();
    }

    @Override
    public Slot getSlot() {
        return delegate.getSlot();
    }

    @Override
    public <T> T getFirstContextBean() {
        return delegate.getFirstContextBean();
    }

    @Override
    public <T> T getContextBean(Class<T> contextBeanClazz) {
        return delegate.getContextBean(contextBeanClazz);
    }

    @Override
    public String getNodeId() {
        return delegate.getNodeId();
    }

    @Override
    public void setNodeId(String nodeId) {
        delegate.setNodeId(nodeId);
    }

    @Override
    public NodeComponent getSelf() {
        return delegate.getSelf();
    }

    @Override
    public void setSelf(NodeComponent self) {
        delegate.setSelf(self);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public NodeTypeEnum getType() {
        return delegate.getType();
    }

    @Override
    public void setType(NodeTypeEnum type) {
        delegate.setType(type);
    }

    @Override
    public <T> void sendPrivateDeliveryData(String nodeId, T t) {
        delegate.sendPrivateDeliveryData(nodeId, t);
    }

    @Override
    public <T> T getPrivateDeliveryData() {
        return delegate.getPrivateDeliveryData();
    }

    @Override
    public Class<? extends Exception>[] getRetryForExceptions() {
        return delegate.getRetryForExceptions();
    }

    @Override
    public void setRetryForExceptions(Class<? extends Exception>[] retryForExceptions) {
        delegate.setRetryForExceptions(retryForExceptions);
    }

    @Override
    public Class<? extends NodeExecutor> getNodeExecutorClass() {
        return delegate.getNodeExecutorClass();
    }

    @Override
    public void setNodeExecutorClass(Class<? extends NodeExecutor> nodeExecutorClass) {
        delegate.setNodeExecutorClass(nodeExecutorClass);
    }

    @Override
    public void setTag(String tag) {
        delegate.setTag(tag);
    }

    @Override
    public String getTag() {
        return delegate.getTag();
    }

    @Override
    public void removeTag() {
        delegate.removeTag();
    }

    @Override
    public String getChainName() {
        return delegate.getChainName();
    }

    @Override
    public String getDisplayName() {
        return delegate.getDisplayName();
    }

    @Override
    public void setCurrChainName(String currChainName) {
        delegate.setCurrChainName(currChainName);
    }

    @Override
    public String getCurrChainName() {
        return delegate.getCurrChainName();
    }

    @Override
    public void removeCurrChainName() {
        delegate.removeCurrChainName();
    }
}
