/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.slot;

import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.exception.NoSuchContextBeanException;
import com.yomahub.liteflow.exception.NullParamException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.property.LiteFlowConfig;
import com.yomahub.liteflow.thread.ExecutorServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Slot的抽象类实现
 *
 * @author Bryan.Zhang
 * @author LeoLee
 */
@SuppressWarnings("unchecked")
public class Slot {

    private static final Logger LOG = LoggerFactory.getLogger(Slot.class);

    private static final String REQUEST = "_request";

    private static final String RESPONSE = "_response";

    private static final String CHAIN_NAME = "_chain_name";

    private static final String SWITCH_NODE_PREFIX = "_switch_";

    private static final String NODE_INPUT_PREFIX = "_input_";

    private static final String NODE_OUTPUT_PREFIX = "_output_";

    private static final String CHAIN_REQ_PREFIX = "_chain_req_";

    private static final String REQUEST_ID = "_req_id";

    private static final String EXCEPTION = "_exception";

    private static final String PRIVATE_DELIVERY_PREFIX = "_private_d_";

    private final Deque<CmpStep> executeSteps = new ConcurrentLinkedDeque<>();

    private String executeStepsStr;

    protected ConcurrentHashMap<String, Object> metaDataMap = new ConcurrentHashMap<>();


    protected ConcurrentHashMap<String, Object> variableMap = new ConcurrentHashMap<>();

    protected ConcurrentHashMap<String, Object> inParameter = new ConcurrentHashMap<>();

    protected ConcurrentHashMap<String, Object> outParameter = new ConcurrentHashMap<>();

    protected ConcurrentHashMap<String, Object> properties = new ConcurrentHashMap<>();

    private Map<String, Object> contextBeanMap = new LinkedHashMap<>();

    private ExecutorServiceManager executorServiceManager;

    private LiteFlowConfig liteflowConfig;

    private FlowConfiguration flowConfiguration;

    public Slot(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
        this.liteflowConfig = flowConfiguration.getLiteflowConfig();
    }

    public Slot(FlowConfiguration flowConfiguration, Map<String, Object> contextBeanMap) {
        this.flowConfiguration = flowConfiguration;
        if (contextBeanMap != null && !contextBeanMap.isEmpty()) {
            this.contextBeanMap.putAll(contextBeanMap);
        }
        this.liteflowConfig = flowConfiguration.getLiteflowConfig();
    }

    private boolean hasMetaData(String key) {
        return metaDataMap.containsKey(key);
    }

    private <T> void putMetaDataMap(String key, T t) {
        if (ObjectUtil.isNull(t)) {
            //data slot is a ConcurrentHashMap, so null value will trigger NullPointerException
            throw new NullParamException("data slot can't accept null param");
        }
        metaDataMap.put(key, t);
    }

    public <T> T getInput(String nodeId) {
        return (T) metaDataMap.get(NODE_INPUT_PREFIX + nodeId);
    }

    public <T> T getOutput(String nodeId) {
        return (T) metaDataMap.get(NODE_OUTPUT_PREFIX + nodeId);
    }

    public <T> void setInput(String nodeId, T t) {
        putMetaDataMap(NODE_INPUT_PREFIX + nodeId, t);
    }

    public <T> void setOutput(String nodeId, T t) {
        putMetaDataMap(NODE_OUTPUT_PREFIX + nodeId, t);
    }


    public <T> T getResponseData() {
        return (T) metaDataMap.get(RESPONSE);
    }

    public <T> void setResponseData(T t) {
        putMetaDataMap(RESPONSE, t);
    }

    public <T> void setPrivateDeliveryData(String nodeId, T t) {
        String privateDKey = PRIVATE_DELIVERY_PREFIX + nodeId;
        synchronized (this) {
            if (metaDataMap.containsKey(privateDKey)) {
                Queue<T> queue = (Queue<T>) metaDataMap.get(privateDKey);
                queue.add(t);
            } else {
                Queue<T> queue = new ConcurrentLinkedQueue<>();
                queue.add(t);
                this.putMetaDataMap(privateDKey, queue);
            }
        }
    }

    public <T> Queue<T> getPrivateDeliveryQueue(String nodeId) {
        String privateDKey = PRIVATE_DELIVERY_PREFIX + nodeId;
        if (metaDataMap.containsKey(privateDKey)) {
            return (Queue<T>) metaDataMap.get(privateDKey);
        } else {
            return null;
        }
    }

    public <T> T getPrivateDeliveryData(String nodeId) {
        String privateDKey = PRIVATE_DELIVERY_PREFIX + nodeId;
        if (metaDataMap.containsKey(privateDKey)) {
            Queue<T> queue = (Queue<T>) metaDataMap.get(privateDKey);
            return queue.poll();
        } else {
            return null;
        }
    }

    public <T> void setSwitchResult(String key, T t) {
        putMetaDataMap(SWITCH_NODE_PREFIX + key, t);
    }

    public <T> T getSwitchResult(String key) {
        return (T) metaDataMap.get(SWITCH_NODE_PREFIX + key);
    }

    public void setChainName(String chainName) {
        if (!hasMetaData(CHAIN_NAME)) {
            this.putMetaDataMap(CHAIN_NAME, chainName);
        }
    }

    public String getChainName() {
        return (String) metaDataMap.get(CHAIN_NAME);
    }

    public void addStep(CmpStep step) {
        this.executeSteps.add(step);
    }

    public CmpStep getFirstStep() {
        return this.executeSteps.getFirst();
    }

    public CmpStep getLastStep() {
        return this.executeSteps.getLast();
    }

    public String getExecuteStepStr(boolean withTimeSpent) {
        StringBuilder str = new StringBuilder();
        CmpStep cmpStep;
        for (Iterator<CmpStep> it = executeSteps.iterator(); it.hasNext(); ) {
            cmpStep = it.next();
            if (withTimeSpent) {
                str.append(cmpStep.buildStringWithTime());
            } else {
                str.append(cmpStep.buildString());
            }
            if (it.hasNext()) {
                str.append("==>");
            }
        }
        this.executeStepsStr = str.toString();
        return this.executeStepsStr;
    }

    public String getExecuteStepStr() {
        return getExecuteStepStr(false);
    }

    public void printStep() {
        if (ObjectUtil.isNull(this.executeStepsStr)) {
            this.executeStepsStr = getExecuteStepStr(true);
        }
        LOG.info("[{}]:CHAIN_NAME[{}]\n{}", getRequestId(), this.getChainName(), this.executeStepsStr);
    }

    public void generateRequestId() {
        setRequestId(flowConfiguration.getRequestIdGenerator().generate());
    }

    public void setRequestId(String requestId) {
        metaDataMap.put(REQUEST_ID, requestId);
    }

    public String getRequestId() {
        return (String) metaDataMap.get(REQUEST_ID);
    }

    public Queue<CmpStep> getExecuteSteps() {
        return executeSteps;
    }

    public Throwable getException() {
        return (Throwable) this.metaDataMap.get(EXCEPTION);
    }

    public void setException(Throwable e) {
        putMetaDataMap(EXCEPTION, e);
    }

    public Map<String, Object> getContextBeanMap() {
        return this.contextBeanMap;
    }

    public <T> T getContentBean(String key) {
        if (!contextBeanMap.containsKey(key)) {
            throw new NoSuchContextBeanException("this type is not in the context type passed in");
        }
        return (T) contextBeanMap.get(key);
    }

    public <T> T getContextBean(Class<T> contextBeanClazz) {
        T t = (T) contextBeanMap.values().stream().filter(o -> o.getClass().isAssignableFrom(contextBeanClazz))
                .findFirst()
                .orElse(null);
        if (t == null) {
            throw new NoSuchContextBeanException("this type is not in the context type passed in");
        }
        return t;
    }

    public <T> T getFirstContextBean() {
        if (contextBeanMap.isEmpty()) {
            return null;
        }
        Optional<Map.Entry<String, Object>> firstKey = this.contextBeanMap.entrySet().stream().findFirst();

        return (T) firstKey.get().getValue();
    }

    public ExecutorServiceManager getExecutorServiceManager() {
        return executorServiceManager;
    }

    public Slot setExecutorServiceManager(ExecutorServiceManager executorServiceManager) {
        this.executorServiceManager = executorServiceManager;
        return this;
    }

    public LiteFlowConfig getLiteflowConfig() {
        return liteflowConfig;
    }

    public boolean hasVariable(String key) {
        return variableMap.containsKey(key);
    }

    public <T> T getVariableByType(String key) {
        return (T) variableMap.get(key);
    }


    public Map<String, Object> variables() {
        return variableMap;
    }

    public Object getVariable(String key) {
        return variableMap.get(key);
    }

    public void putVariable(String key, Object value) {
        variableMap.put(key, value);
    }

    public void removeVariable(String key) {
        variableMap.remove(key);
    }

    public void clearVariable() {
        variableMap.clear();
    }

    public Map<String, Object> _putInParameter(String key, Object value) {
        this.inParameter.put(key, value);
        return inParameter;
    }

    public Map<String, Object> putParameter(String key, Object value) {
        this.outParameter.put(key, value);
        return this.outParameter;
    }

    public Object getParameter(String key) {
        return inParameter.get(key);
    }

    public <T> T getParameterByType(String key) {
        return (T) inParameter.get(key);
    }

    public void removeParameter(String key) {
        outParameter.remove(key);
    }

    public boolean hasParameter(String key) {
        return inParameter.containsKey(key);
    }

    public void _swapParameter() {
        if (!inParameter.isEmpty()) {
            inParameter.clear();
        }
        if (!outParameter.isEmpty()) {
            inParameter.putAll(outParameter);
            outParameter.clear();
        }
    }

    public void putProperties(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public <T> T getPropertyByType(String key) {
        return (T) properties.get(key);
    }

    public void clearProperties() {
        this.properties.clear();
    }

    public boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }

    public FlowConfiguration getFlowConfiguration() {
        return this.getFlowConfiguration();
    }
}
