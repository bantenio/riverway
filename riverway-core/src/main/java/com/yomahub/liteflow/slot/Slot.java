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
public interface Slot {
    
    String RESPONSE = "_response";

    String CHAIN_NAME = "_chain_name";

    String SWITCH_NODE_PREFIX = "_switch_";

    String NODE_INPUT_PREFIX = "_input_";

    String NODE_OUTPUT_PREFIX = "_output_";

    String REQUEST_ID = "_req_id";

    String EXCEPTION = "_exception";

    String PRIVATE_DELIVERY_PREFIX = "_private_d_";

    boolean hasMetaData(String key);

    <T> void putMetaDataMap(String key, T t);

    <T> T getInput(String nodeId);

    <T> T getOutput(String nodeId);

    <T> void setInput(String nodeId, T t);

    <T> void setOutput(String nodeId, T t);


    <T> T getResponseData();
    <T> void setResponseData(T t);

    <T> void setPrivateDeliveryData(String nodeId, T t);

    <T> Queue<T> getPrivateDeliveryQueue(String nodeId);

    <T> T getPrivateDeliveryData(String nodeId);

    <T> void setSwitchResult(String key, T t);

    <T> T getSwitchResult(String key);

    void setChainName(String chainName);

    String getChainName();

    void addStep(CmpStep step);

    CmpStep getFirstStep();

    CmpStep getLastStep();

    String getExecuteStepStr(boolean withTimeSpent);

    String getExecuteStepStr();

    void printStep();

    void generateRequestId();

    void setRequestId(String requestId);

    String getRequestId();

    Queue<CmpStep> getExecuteSteps();

    Throwable getException();

    void setException(Throwable e);

    Map<String, Object> getContextBeanMap();

    <T> T getContentBean(String key);

    <T> T getContextBean(Class<T> contextBeanClazz);

    <T> T getFirstContextBean();

    ExecutorServiceManager getExecutorServiceManager();

    Slot setExecutorServiceManager(ExecutorServiceManager executorServiceManager);

    LiteFlowConfig getLiteflowConfig();

    boolean hasVariable(String key);

    <T> T getVariableByType(String key);


    Map<String, Object> variables();

    Object getVariable(String key);

    void putVariable(String key, Object value);

    void removeVariable(String key);

    void clearVariable();

    Map<String, Object> _putInParameter(String key, Object value);

    Map<String, Object> putParameter(String key, Object value);

    Object getParameter(String key);

    <T> T getParameterByType(String key);

    void removeParameter(String key);

    boolean hasParameter(String key);

    void _swapParameter();

    void putProperties(Map<String, Object> properties);

    Object getProperty(String key);

    <T> T getPropertyByType(String key);

    void clearProperties();

    boolean hasProperty(String key);

    FlowConfiguration getFlowConfiguration();

    int getIndex();
}
