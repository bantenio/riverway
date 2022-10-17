package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;

import java.util.List;

public class ChainProxy extends Chain {
    private final Chain delegate;

    private String id;

    public ChainProxy(Chain delegate, String id) {
        this.delegate = delegate;
        this.id = id;
    }

    public ChainProxy(Chain delegate) {
        this.delegate = delegate;
    }

    public String getId() {
        return id;
    }

    public ChainProxy setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public List<Condition> getConditionList() {
        return delegate.getConditionList();
    }

    @Override
    public Chain setConditionList(List<Condition> conditionList) {
        return delegate.setConditionList(conditionList);
    }

    @Override
    public String getChainName() {
        return delegate.getChainName();
    }

    @Override
    public Chain setChainName(String chainName) {
        return delegate.setChainName(chainName);
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
    public void executeConditions(Integer slotIndex, List<Condition> conditionList, String chainName, FlowConfiguration flowConfiguration) throws Throwable {
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
    public List<Condition> getPreConditionList() {
        return delegate.getPreConditionList();
    }

    @Override
    public Chain setPreConditionList(List<Condition> preConditionList) {
        return delegate.setPreConditionList(preConditionList);
    }

    @Override
    public List<Condition> getFinallyConditionList() {
        return delegate.getFinallyConditionList();
    }

    @Override
    public Chain setFinallyConditionList(List<Condition> finallyConditionList) {
        return delegate.setFinallyConditionList(finallyConditionList);
    }

    @Override
    public boolean isAccess(Integer slotIndex) throws Throwable {
        return delegate.isAccess(slotIndex);
    }

    @Override
    public void setCurrChainName(String currentChainName) {
        delegate.setCurrChainName(currentChainName);
    }
}
