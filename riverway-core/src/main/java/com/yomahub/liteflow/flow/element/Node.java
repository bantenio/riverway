/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.flow.element;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.exception.FlowSystemException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.flow.executor.NodeExecutor;
import com.yomahub.liteflow.flow.executor.NodeExecutorHelper;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Node节点，实现可执行器
 *
 * @author Bryan.Zhang
 */
public class Node implements Executable<Node>, Cloneable {

    private static final Logger log = LoggerFactory.getLogger(Node.class);

    private String id;

    private String name;

    private String clazz;

    private NodeTypeEnum type;

    private String script;

    private NodeComponent instance;

    private String tag;

    //重试次数
    private int retryCount = 0;

    public Node() {
    }

    public Node(NodeComponent instance) {
        this(instance.getNodeId(), instance.getName(), instance.getType(), instance.getClass().getName());
        this.instance = instance;
    }

    public Node(String id, String name, NodeTypeEnum type, String clazz) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.clazz = clazz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeTypeEnum getType() {
        return type;
    }

    public void setType(NodeTypeEnum type) {
        this.type = type;
    }

    public NodeComponent getInstance() {
        return instance;
    }

    public void setInstance(NodeComponent instance) {
        this.instance = instance;
    }

    protected NodeComponent getInstance(FlowConfiguration flowConfiguration) {
        if (ObjectUtil.isNull(instance)) {
            synchronized (this) {
                if (ObjectUtil.isNull(instance)) {
                    instance = flowConfiguration.getNodeComponent(getId());
                }
            }
        }
        return instance;
    }

    @Override
    public Node getSelf() {
        return this;
    }

    //node的执行主要逻辑
    //所有的可执行节点，其实最终都会落到node上来，因为chain中包含的也是node
    @Override
    public void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        NodeComponent instance = getInstance(flowConfiguration);
        try {
            //判断是否可执行，所以isAccess经常作为一个组件进入的实际判断要素，用作检查slot里的参数的完备性
            if (instance.isAccess()) {
                //根据配置判断是否打印执行中的日志
                log.info("[{}]:[O]start component[{}] execution", slot.getRequestId(), getDisplayName());

                //这里开始进行重试的逻辑和主逻辑的运行
                NodeExecutor nodeExecutor = NodeExecutorHelper.loadInstance().buildNodeExecutor(instance.getNodeExecutorClass());
                // 调用节点执行器进行执行
                instance.setSlotIndex(slot.getIndex());
                instance.setSelf(instance);
                nodeExecutor.execute(this, slot, flowConfiguration);
                //如果组件覆盖了isEnd方法，或者在在逻辑中主要调用了setEnd(true)的话，流程就会立马结束
                if (instance.isEnd()) {
                    String errorInfo = StrUtil.format("[{}]:[{}] lead the chain to end", slot.getRequestId(), getDisplayName());
                    throw new ChainEndException(errorInfo);
                }
            } else {
                log.info("[{}]:[X]skip component[{}] execution", slot.getRequestId(), instance.getDisplayName());
            }
        } catch (ChainEndException e) {
            throw e;
        } catch (Exception e) {
            //如果组件覆盖了isContinueOnError方法，返回为true，那即便出了异常，也会继续流程
            if (instance.isContinueOnError()) {
                String errorMsg = MessageFormat.format("[{0}]:component[{1}] cause error,but flow is still go on", slot.getRequestId(), getExecuteName());
                log.error(errorMsg);
            } else {
                String errorMsg = MessageFormat.format("[{0}]:component[{1}] cause error,error:{2}", slot.getRequestId(), getExecuteName(), e.getMessage());
                log.error(errorMsg);
                throw e;
            }
        } finally {
            //移除threadLocal里的信息
            instance.removeSlotIndex();
            instance.removeIsEnd();
            instance.removeTag();
            instance.removeCurrChainName();
        }
    }

    @Override
    public Object processWithResult(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        NodeComponent instance = getInstance(flowConfiguration);
        Object result = null;
        try {
            //判断是否可执行，所以isAccess经常作为一个组件进入的实际判断要素，用作检查slot里的参数的完备性
            if (instance.isAccess()) {
                //根据配置判断是否打印执行中的日志
                log.info("[{}]:[O]start component[{}] execution", slot.getRequestId(), getDisplayName());

                //这里开始进行重试的逻辑和主逻辑的运行
                NodeExecutor nodeExecutor = NodeExecutorHelper.loadInstance().buildNodeExecutor(instance.getNodeExecutorClass());
                // 调用节点执行器进行执行
                instance.setSlotIndex(slot.getIndex());
                instance.setSelf(instance);
                result = nodeExecutor.execute(this, slot, flowConfiguration);
                //如果组件覆盖了isEnd方法，或者在在逻辑中主要调用了setEnd(true)的话，流程就会立马结束
                if (instance.isEnd()) {
                    String errorInfo = StrUtil.format("[{}]:[{}] lead the chain to end", slot.getRequestId(), getDisplayName());
                    throw new ChainEndException(errorInfo);
                }
            } else {
                log.info("[{}]:[X]skip component[{}] execution", slot.getRequestId(), instance.getDisplayName());
            }
        } catch (ChainEndException e) {
            throw e;
        } catch (Exception e) {
            //如果组件覆盖了isContinueOnError方法，返回为true，那即便出了异常，也会继续流程
            if (instance.isContinueOnError()) {
                String errorMsg = MessageFormat.format("[{0}]:component[{1}] cause error,but flow is still go on", slot.getRequestId(), getExecuteName());
                log.error(errorMsg);
            } else {
                String errorMsg = MessageFormat.format("[{0}]:component[{1}] cause error,error:{2}", slot.getRequestId(), getExecuteName(), e.getMessage());
                log.error(errorMsg);
                throw e;
            }
        } finally {
            //移除threadLocal里的信息
            instance.removeSlotIndex();
            instance.removeIsEnd();
            instance.removeTag();
            instance.removeCurrChainName();
        }
        return result;
    }

    //在同步场景并不会单独执行这方法，同步场景会在execute里面去判断isAccess。
    //但是在异步场景的any=true情况下，如果isAccess返回了false，那么异步的any有可能会认为这个组件先执行完。就会导致不正常
    //增加这个方法是为了在异步的时候，先去过滤掉isAccess为false的异步组件。然后再异步执行。
    //详情见这个issue:https://gitee.com/dromara/liteFlow/issues/I4XRBA
    @Override
    public boolean isAccess(Slot slot) throws Exception {
        instance.setSlotIndex(slot.getIndex());
        return instance.isAccess();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Node copy() throws Exception {
        return (Node) this.clone();
    }

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return ExecuteTypeEnum.NODE;
    }

    @Override
    public String getExecuteName() {
        return StrUtil.blankToDefault(id, name);
    }

    public String getDisplayName() {
        return StrUtil.blankToDefault(instance.getDisplayName(), getExecuteName());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public Node setCurrChainName(String currentChainName) {
        instance.setCurrChainName(currentChainName);
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public boolean hasResult() {
        return instance.hasResult();
    }
}
