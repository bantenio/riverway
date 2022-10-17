/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.flow.element;

import cn.hutool.core.collection.CollUtil;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.exception.ChainEndException;
import com.yomahub.liteflow.exception.FlowSystemException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.condition.Condition;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * chain对象，实现可执行器
 *
 * @author Bryan.Zhang
 */
public class Chain implements Executable {

    private static final Logger log = LoggerFactory.getLogger(Chain.class);

    private String chainName;

    private List<Condition> conditionList = new ArrayList<>();

    //前置处理Condition，用来区别主体的Condition
    private List<Condition> preConditionList = new ArrayList<>();

    //后置处理Condition，用来区别主体的Condition
    private List<Condition> finallyConditionList = new ArrayList<>();

    public Chain(String chainName) {
        this.chainName = chainName;
    }

    public Chain() {
    }

    public Chain(String chainName, List<Condition> conditionList) {
        this.chainName = chainName;
        this.conditionList = conditionList;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public Chain setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
        return this;
    }

    public String getChainName() {
        return chainName;
    }

    public Chain setChainName(String chainName) {
        this.chainName = chainName;
        return this;
    }

    //执行chain的主方法
    @Override
    public void process(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        if (CollUtil.isEmpty(conditionList)) {
            throw new FlowSystemException("no conditionList in this chain[" + chainName + "]");
        }
        Slot slot = DataBus.getSlot(slotIndex);
        try {
            //设置主ChainName
            slot.setChainName(chainName);
            //执行前置
            this.executePre(slotIndex, flowConfiguration);
            this.executeConditions(slotIndex, conditionList, chainName, flowConfiguration);
            this.executeAfter(slotIndex, flowConfiguration);
        } catch (ChainEndException e) {
            //这里单独catch ChainEndException是因为ChainEndException是用户自己setIsEnd抛出的异常
            //是属于正常逻辑，所以会在FlowExecutor中判断。这里不作为异常处理
            throw e;
        } catch (Throwable e) {
            //这里事先取到exception set到slot里，为了方便finally取到exception
            slot.setException(e);
            this.executeException(slotIndex, e, flowConfiguration);
            throw e;
        } finally {
            //执行后置
            this.executeFinally(slotIndex, flowConfiguration);
        }
    }

    public void executeConditions(Integer slotIndex, List<Condition> conditionList, String chainName, FlowConfiguration flowConfiguration) throws Throwable {
        //执行主体Condition
        for (Condition condition : conditionList) {
            condition.setCurrChainName(chainName);
            condition.execute(slotIndex, flowConfiguration);
        }
    }

    // 执行pre节点
    public void executePre(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        for (Condition condition : this.preConditionList) {
            condition.execute(slotIndex, flowConfiguration);
        }
    }

    public void executeException(Integer slotIndex, Throwable e, FlowConfiguration flowConfiguration) throws Throwable {
    }

    public void executeAfter(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
    }

    //执行后置
    public void executeFinally(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        for (Condition condition : this.finallyConditionList) {
            condition.execute(slotIndex, flowConfiguration);
        }
    }

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return ExecuteTypeEnum.CHAIN;
    }

    @Override
    public String getExecuteName() {
        return chainName;
    }

    public List<Condition> getPreConditionList() {
        return preConditionList;
    }

    public Chain setPreConditionList(List<Condition> preConditionList) {
        this.preConditionList = preConditionList;
        return this;
    }

    public List<Condition> getFinallyConditionList() {
        return finallyConditionList;
    }

    public Chain setFinallyConditionList(List<Condition> finallyConditionList) {
        this.finallyConditionList = finallyConditionList;
        return this;
    }
}
