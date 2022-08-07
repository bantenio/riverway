/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.slot;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.exception.NoSuchContextBeanException;
import com.yomahub.liteflow.exception.NullParamException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.property.LiteFlowConfig;
import com.yomahub.liteflow.thread.ExecutorServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Slot的抽象类实现
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

    private final Queue<CmpStep> executeSteps = new ConcurrentLinkedQueue<>();

    private String executeStepsStr;

    protected ConcurrentHashMap<String, Object> metaDataMap = new ConcurrentHashMap<>();

    private List<Object> contextBeanList;

    private ExecutorServiceManager executorServiceManager;

    private LiteFlowConfig liteflowConfig;

    private FlowConfiguration flowConfiguration;

    public Slot(FlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
        this.liteflowConfig = flowConfiguration.getLiteflowConfig();
    }

    public Slot(FlowConfiguration flowConfiguration, List<Object> contextBeanList) {
        this.flowConfiguration = flowConfiguration;
        this.contextBeanList = contextBeanList;
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

    public <T> T getRequestData() {
        return (T) metaDataMap.get(REQUEST);
    }

    public <T> void setRequestData(T t) {
        putMetaDataMap(REQUEST, t);
    }

    public <T> T getResponseData() {
        return (T) metaDataMap.get(RESPONSE);
    }

    public <T> void setResponseData(T t) {
        putMetaDataMap(RESPONSE, t);
    }

    public <T> T getChainReqData(String chainId) {
        String key = CHAIN_REQ_PREFIX + chainId;
        if (hasMetaData(key)) {
            Queue<Object> queue = (Queue<Object>) metaDataMap.get(key);
            return (T) queue.poll();
        } else {
            return null;
        }
    }

    public synchronized <T> void setChainReqData(String chainId, T t) {
        String key = CHAIN_REQ_PREFIX + chainId;
        if (hasMetaData(key)) {
            Queue<Object> queue = (Queue<Object>) metaDataMap.get(key);
            queue.offer(t);
        } else {
            putMetaDataMap(key, new ConcurrentLinkedQueue<>(ListUtil.toList(t)));
        }
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

    public Exception getException() {
        return (Exception) this.metaDataMap.get(EXCEPTION);
    }

    public void setException(Exception e) {
        putMetaDataMap(EXCEPTION, e);
    }

    public List<Object> getContextBeanList() {
        return this.contextBeanList;
    }

    public <T> T getContextBean(Class<T> contextBeanClazz) {
        T t = (T) contextBeanList.stream().filter(o -> o.getClass().isAssignableFrom(contextBeanClazz))
                .findFirst()
                .orElse(null);
        if (t == null) {
            throw new NoSuchContextBeanException("this type is not in the context type passed in");
        }
        return t;
    }

    public <T> T getFirstContextBean() {
        Class<T> firstContextBeanClazz = (Class<T>) this.getContextBeanList().get(0).getClass();
        return this.getContextBean(firstContextBeanClazz);
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
}
