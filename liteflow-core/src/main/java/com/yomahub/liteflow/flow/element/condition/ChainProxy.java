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

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<Condition> getConditionList() {
        return delegate.getConditionList();
    }

    @Override
    public void setConditionList(List<Condition> conditionList) {
        delegate.setConditionList(conditionList);
    }

    @Override
    public String getChainName() {
        return delegate.getChainName();
    }

    @Override
    public void setChainName(String chainName) {
        delegate.setChainName(chainName);
    }

    @Override
    public void execute(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.execute(slotIndex, flowConfiguration);
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
    public void setPreConditionList(List<Condition> preConditionList) {
        delegate.setPreConditionList(preConditionList);
    }

    @Override
    public List<Condition> getFinallyConditionList() {
        return delegate.getFinallyConditionList();
    }

    @Override
    public void setFinallyConditionList(List<Condition> finallyConditionList) {
        delegate.setFinallyConditionList(finallyConditionList);
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
