/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.core;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.yomahub.liteflow.core.ext.NodeProcessor;
import com.yomahub.liteflow.enums.CmpStepTypeEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.flow.executor.DefaultNodeExecutor;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 普通组件抽象类
 *
 * @author Bryan.Zhang
 */
public abstract class NodeComponent implements NodeProcessor {
    private static final Logger log = LoggerFactory.getLogger(NodeComponent.class);

    private String nodeId;

    private String name;

    private NodeTypeEnum type;

    //这是自己的实例，取代this
    //为何要设置这个，用this不行么，因为如果有aop去切的话，this在spring的aop里是切不到的。self对象有可能是代理过的对象
    private NodeComponent self;

    //在目标异常抛出时才重试
    private Class<? extends Exception>[] retryForExceptions = new Class[]{Exception.class};

    /**
     * 节点执行器的类全名
     */
    private Class<? extends NodeExecutor> nodeExecutorClass = DefaultNodeExecutor.class;

    /********************以下的属性为线程附加属性，并非不变属性********************/

    //当前slot的index
    private final TransmittableThreadLocal<Integer> slotIndexTL = new TransmittableThreadLocal<>();

    //是否结束整个流程，这个只对串行流程有效，并行流程无效
    private final TransmittableThreadLocal<Boolean> isEndTL = new TransmittableThreadLocal<>();

    //tag标签
    private final TransmittableThreadLocal<String> tagTL = new TransmittableThreadLocal<>();

    //当前流程名字
    private final TransmittableThreadLocal<String> currChainNameTL = new TransmittableThreadLocal<>();

    public NodeComponent() {
    }

    public Object execute(Node node, boolean isRetry) throws Throwable {
        return executeInner(node, isRetry);
    }

    protected Object executeInner(Node node, boolean isRetry) throws Throwable {
        Slot slot = this.getSlot();

        //在元数据里加入step信息
        CmpStep cmpStep = new CmpStep(slot.getChainName(), node.getExecuteName(), node.getDisplayName(), CmpStepTypeEnum.SINGLE);
        cmpStep.setTag(tagTL.get());
        slot.addStep(cmpStep);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = null;

        try {
            //前置处理
            self.beforeProcess(this.getNodeId(), slot);

            //主要的处理逻辑
            result = self.process(node);

            //成功后回调方法
            self.onSuccess(node);

            //步骤状态设为true
            cmpStep.setSuccess(true);
        } catch (Exception e) {
            //步骤状态设为false，并加入异常
            cmpStep.setSuccess(false);
            cmpStep.setException(e);

            //执行失败后回调方法
            //这里要注意，失败方法本身抛出错误，只打出堆栈，往外抛出的还是主要的异常
            try {
                self.onError(node);
            } catch (Exception ex) {
                String errMsg = StrUtil.format("[{}]:component[{}] onError method happens exception", slot.getRequestId(), this.getDisplayName());
                log.error(errMsg);
            }
            throw e;
        } finally {
            stopWatch.stop();
            final long timeSpent = stopWatch.getTotalTimeMillis();
            log.debug("[{}]:component[{}] finished in {} milliseconds", slot.getRequestId(), this.getDisplayName(), timeSpent);

            //往CmpStep中放入时间消耗信息
            cmpStep.setTimeSpent(timeSpent);

            //后置处理
            self.afterProcess(this.getNodeId(), slot);
            slot._swapParameter();
        }
        return result;
    }

    public void process(Node node, Slot slot) throws Throwable {
    }

    public Object processWithResult(Node node, Slot slot) throws Throwable {
        return null;
    }


    public <T> void beforeProcess(String nodeId, Slot slot) {
    }

    public void onSuccess(Node node) throws Throwable {
        //如果需要在成功后回调某一个方法，请覆盖这个方法
    }

    public void onError(Node node) throws Throwable {
        //如果需要在抛错后回调某一段逻辑，请覆盖这个方法
    }

    public <T> void afterProcess(String nodeId, Slot slot) {
    }

    //是否进入该节点
    public boolean isAccess() {
        return true;
    }

    //出错是否继续执行(这个只适用于串行流程，并行节点不起作用)
    public boolean isContinueOnError() {
        return false;
    }

    //是否结束整个流程(不往下继续执行)
    public boolean isEnd() {
        Boolean isEnd = isEndTL.get();
        if (ObjectUtil.isNull(isEnd)) {
            return false;
        } else {
            return isEndTL.get();
        }
    }

    //设置是否结束整个流程
    public void setIsEnd(boolean isEnd) {
        this.isEndTL.set(isEnd);
    }

    public void removeIsEnd() {
        this.isEndTL.remove();
    }

    public NodeComponent setSlotIndex(Integer slotIndex) {
        this.slotIndexTL.set(slotIndex);
        return this;
    }

    public Integer getSlotIndex() {
        return this.slotIndexTL.get();
    }

    public void removeSlotIndex() {
        this.slotIndexTL.remove();
    }

    @Override
    public Slot getSlot() {
        return DataBus.getSlot(this.slotIndexTL.get());
    }

    public <T> T getFirstContextBean() {
        return this.getSlot().getFirstContextBean();
    }

    public <T> T getContextBean(Class<T> contextBeanClazz) {
        return this.getSlot().getContextBean(contextBeanClazz);
    }

    public String getNodeId() {
        return nodeId;
    }

    public NodeComponent setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public NodeComponent getSelf() {
        return self;
    }

    public void setSelf(NodeComponent self) {
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public NodeComponent setName(String name) {
        this.name = name;
        return this;
    }

    public NodeTypeEnum getType() {
        return type;
    }

    public void setType(NodeTypeEnum type) {
        this.type = type;
    }

    public <T> void sendPrivateDeliveryData(String nodeId, T t) {
        this.getSlot().setPrivateDeliveryData(nodeId, t);
    }

    public <T> T getPrivateDeliveryData() {
        return this.getSlot().getPrivateDeliveryData(this.getNodeId());
    }

    public Class<? extends Exception>[] getRetryForExceptions() {
        return retryForExceptions;
    }

    public void setRetryForExceptions(Class<? extends Exception>[] retryForExceptions) {
        this.retryForExceptions = retryForExceptions;
    }

    public Class<? extends NodeExecutor> getNodeExecutorClass() {
        return nodeExecutorClass;
    }

    public void setNodeExecutorClass(Class<? extends NodeExecutor> nodeExecutorClass) {
        this.nodeExecutorClass = nodeExecutorClass;
    }

    public void setTag(String tag) {
        this.tagTL.set(tag);
    }

    public String getTag() {
        return this.tagTL.get();
    }

    public void removeTag() {
        this.tagTL.remove();
    }

    public String getChainName() {
        return getSlot().getChainName();
    }

    public String getDisplayName() {
        if (StrUtil.isEmpty(this.name)) {
            return this.nodeId;
        } else {
            return StrUtil.format("{}({})", this.nodeId, this.name);
        }
    }

    public void setCurrChainName(String currChainName) {
        this.currChainNameTL.set(currChainName);
    }

    public String getCurrChainName() {
        return this.currChainNameTL.get();
    }

    public void removeCurrChainName() {
        this.currChainNameTL.remove();
    }
}
