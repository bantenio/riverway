/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.core;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.exception.ChainNotFoundException;
import com.yomahub.liteflow.exception.NoAvailableSlotException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.plugins.PluginManager;
import com.yomahub.liteflow.plugins.support.InterceptorChainProxy;
import com.yomahub.liteflow.property.ExecutorProperties;
import com.yomahub.liteflow.property.LiteFlowConfig;
import com.yomahub.liteflow.property.LogProperties;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.DefaultContext;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 流程规则主要执行器类
 *
 * @author Bryan.Zhang
 */
public class FlowExecutor {

    private static final Logger log = LoggerFactory.getLogger(FlowExecutor.class);

    private final LogProperties logConfig;

    private final ExecutorProperties executorProperties;

    private final LiteFlowConfig liteflowConfig;

    private final FlowConfiguration flowConfiguration;

    public FlowExecutor(LiteFlowConfig liteflowConfig,
                        FlowConfiguration flowConfiguration) {
        this.liteflowConfig = liteflowConfig;
        this.logConfig = liteflowConfig.getLogConfig();
        this.executorProperties = liteflowConfig.getExecutorProperties();
        this.flowConfiguration = flowConfiguration;
        //初始化DataBus
        DataBus.init(liteflowConfig);
    }

    //隐式流程的调用方法
    public void invoke(String chainId, Slot slot, Map<String, Object> params) throws Throwable {
        LiteflowResponse response = this.execute2Resp(chainId, params, slot, true);
        if (!response.isSuccess()) {
            throw response.getCause();
        }
    }

    public LiteflowResponse invoke2Resp(String chainId, Slot slot, Map<String, Object> params) {
        return this.execute2Resp(chainId, params, slot, true);
    }

    //单独调用某一个node
    public void invoke(String nodeId, Slot slot) throws Throwable {
        Node node = flowConfiguration.getNode(nodeId);
        node.execute(slot, flowConfiguration);
    }

    //调用一个流程并返回LiteflowResponse，上下文为默认的DefaultContext，初始参数为null
    public LiteflowResponse execute2Resp(String chainId) {
        return this.execute2Resp(chainId, new HashMap<>());
    }

    //调用一个流程并返回LiteflowResponse，允许多上下文的传入
    public LiteflowResponse execute2Resp(String chainId, Map<String, Object> params) {
        return this.execute2Resp(chainId, params, null, false);
    }

    //调用一个流程并返回Future<LiteflowResponse>，允许多上下文的传入
    public Future<LiteflowResponse> execute2Future(String chainId, Map<String, Object> params) {
        return flowConfiguration.getExecutorServiceManager().get(executorProperties.getMainExecutorServiceName()).submit(()
                -> this.execute2Resp(chainId, params, null, false));
    }

    //调用一个流程，返回默认的上下文，适用于简单的调用
    public DefaultContext execute(String chainId, Map<String, Object> params) throws Throwable {
        LiteflowResponse response = this.execute2Resp(chainId, params);
        if (!response.isSuccess()) {
            throw response.getCause();
        } else {
            return response.getFirstContextBean();
        }
    }

    protected LiteflowResponse execute2Resp(String chainId, Map<String, Object> params, Slot slot, boolean isInnerChain) {
        try {
            slot = doExecute(chainId, params, slot, isInnerChain);
            return new LiteflowResponse(slot);
        } finally {
            if (slot != null) {
                slot.clearVariable();
            }
        }
    }

    protected Slot doExecute(String chainId, Map<String, Object> params, Slot slot, boolean isInnerChain) {

        if (!isInnerChain && ObjectUtil.isNull(slot)) {
            slot = DataBus.offerSlot(params, flowConfiguration);
            if (BooleanUtil.isTrue(logConfig.getPrintExecutionLog())) {
                log.info("slot[{}] offered", slot.getIndex());
            }
        }

        if (StrUtil.isBlank(slot.getRequestId())) {
            slot.generateRequestId();
            if (BooleanUtil.isTrue(logConfig.getPrintExecutionLog())) {
                log.info("requestId[{}] has generated", slot.getRequestId());
            }
        }

        Chain chain = null;
        int slotIndex = slot.getIndex();
        try {
            chain = flowConfiguration.getChain(chainId);

            if (ObjectUtil.isNull(chain)) {
                String errorMsg = StrUtil.format("[{}]:couldn't find chain with the id[{}]", slot.getRequestId(), chainId);
                throw new ChainNotFoundException(errorMsg);
            }
            PluginManager pluginManager = flowConfiguration.getPluginManager();
            if (pluginManager != null && !pluginManager.isEmpty()) {
                chain = new InterceptorChainProxy(chain, flowConfiguration);
            }
            // 执行chain
            chain.execute(slot, flowConfiguration);
        } catch (ChainEndException e) {
            if (ObjectUtil.isNotNull(chain)) {
                String warnMsg = StrUtil.format("[{}]:chain[{}] execute end on slot[{}]", slot.getRequestId(), chain.getChainName(), slotIndex);
                log.warn(warnMsg);
            }
        } catch (Throwable e) {
            if (ObjectUtil.isNotNull(chain)) {
                String errMsg = StrUtil.format("[{}]:chain[{}] execute error on slot[{}]", slot.getRequestId(), chain.getChainName(), slotIndex);
                log.error(errMsg);
            } else {
                log.error(e.getMessage());
            }
            slot.setException(e);
        } finally {
            if (!isInnerChain) {
                slot.printStep();
                DataBus.releaseSlot(slotIndex);
            }
        }
        return slot;
    }
}