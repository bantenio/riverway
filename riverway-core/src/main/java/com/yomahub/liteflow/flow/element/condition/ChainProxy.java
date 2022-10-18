package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;

import java.util.List;

public class ChainProxy extends Chain<ChainProxy> {
    private final Chain<? extends Chain<?>> delegate;

    private String id;

    public ChainProxy(Chain<? extends Chain<?>> delegate, String id) {
        this.delegate = delegate;
        this.id = id;
    }

    public ChainProxy(Chain<? extends Chain<?>> delegate) {
        this.delegate = delegate;
    }

    public String getId() {
        return id;
    }

    public ChainProxy setId(String id) {
        this.id = id;
        return getSelf();
    }

    public ChainProxy id(String id) {
        this.id = id;
        return getSelf();
    }

    @Override
    public List<Condition<? extends Condition<?>>> getConditionList() {
        return delegate.getConditionList();
    }

    @Override
    public ChainProxy setConditionList(List<Condition<? extends Condition<?>>> conditionList) {
        delegate.setConditionList(conditionList);
        return getSelf();
    }

    @Override
    public String getChainName() {
        return delegate.getChainName();
    }

    @Override
    public ChainProxy setChainName(String chainName) {
        delegate.setChainName(chainName);
        return getSelf();
    }

    @Override
    public Object execute(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        return delegate.execute(slotIndex, flowConfiguration);
    }

    @Override
    public void process(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.process(slotIndex, flowConfiguration);
    }

    public Object processWithResult(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        return delegate.processWithResult(slotIndex, flowConfiguration);
    }

    @Override
    public void executeConditions(Integer slotIndex, List<Condition<? extends Condition<?>>> conditionList, String chainName, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeConditions(slotIndex, conditionList, chainName, flowConfiguration);
    }

    @Override
    public void executePre(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executePre(slotIndex, flowConfiguration);
    }

    @Override
    public void executeException(Integer slotIndex, Throwable e, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeException(slotIndex, e, flowConfiguration);
    }

    @Override
    public void executeAfter(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeAfter(slotIndex, flowConfiguration);
    }

    @Override
    public void executeFinally(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeFinally(slotIndex, flowConfiguration);
    }

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return delegate.getExecuteType();
    }

    @Override
    public String getExecuteName() {
        return StrUtil.blankToDefault(id, delegate.getExecuteName());
    }

    @Override
    public List<Condition<? extends Condition<?>>> getPreConditionList() {
        return delegate.getPreConditionList();
    }

    @Override
    public ChainProxy setPreConditionList(List<Condition<? extends Condition<?>>> preConditionList) {
        delegate.setPreConditionList(preConditionList);
        return getSelf();
    }

    @Override
    public List<Condition<? extends Condition<?>>> getFinallyConditionList() {
        return delegate.getFinallyConditionList();
    }

    @Override
    public ChainProxy setFinallyConditionList(List<Condition<? extends Condition<?>>> finallyConditionList) {
        delegate.setFinallyConditionList(finallyConditionList);
        return getSelf();
    }

    @Override
    public boolean isAccess(Integer slotIndex) throws Throwable {
        return delegate.isAccess(slotIndex);
    }

    @Override
    public ChainProxy setCurrChainName(String currentChainName) {
        delegate.setCurrChainName(currentChainName);
        return getSelf();
    }
}
