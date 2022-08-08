package com.yomahub.liteflow.flow.executor;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 节点执行器 - 自定的执行策略需要实现该类
 *
 * @author sikadai
 * @since 2.6.9
 */
public abstract class NodeExecutor {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    //执行器执行入口-若需要更大维度的执行方式可以重写该方法
    public void execute(Node node) throws Exception {
        NodeComponent instance = node.getInstance();
        int retryCount = node.getRetryCount();
        List<Class<? extends Exception>> forExceptions = Arrays.asList(instance.getRetryForExceptions());
        for (int i = 0; i <= retryCount; i++) {
            try {
                // 先执行一次
                if (i == 0) {
                    instance.execute(node);
                } else {
                    // 进入重试逻辑
                    retry(instance, i, node);
                }
                break;
            } catch (ChainEndException e) {
                //如果是ChainEndException，则无需重试
                throw e;
            } catch (Exception e) {
                //判断抛出的异常是不是指定异常的子类
                boolean flag = forExceptions.stream().anyMatch(clazz -> clazz.isAssignableFrom(e.getClass()));
                //两种情况不重试，1)抛出异常不在指定异常范围内 2)已经重试次数大于等于配置次数
                if (!flag || i >= retryCount) {
                    throw e;
                }
            }
        }
    }

    //执行重试逻辑 - 子类通过实现该方法进行重试逻辑的控制
    protected void retry(NodeComponent instance, int currentRetryCount, Node node) throws Exception {
        Slot slot = DataBus.getSlot(instance.getSlotIndex());
        log.info("[{}]:component[{}] performs {} retry", slot.getRequestId(), instance.getDisplayName(), currentRetryCount + 1);
        //执行业务逻辑的主要入口
        instance.execute(node);
    }
}
