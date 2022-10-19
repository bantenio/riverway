package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.slot.Slot;

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
    public Object execute(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        return delegate.execute(slot, flowConfiguration);
    }

    @Override
    public void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.process(slot, flowConfiguration);
    }

    public Object processWithResult(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        return delegate.processWithResult(slot, flowConfiguration);
    }

    @Override
    public void executeConditions(Slot slot, List<Condition<? extends Condition<?>>> conditionList, String chainName, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeConditions(slot, conditionList, chainName, flowConfiguration);
    }

    @Override
    public void executePre(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executePre(slot, flowConfiguration);
    }

    @Override
    public void executeException(Slot slot, Throwable e, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeException(slot, e, flowConfiguration);
    }

    @Override
    public void executeAfter(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeAfter(slot, flowConfiguration);
    }

    @Override
    public void executeFinally(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        delegate.executeFinally(slot, flowConfiguration);
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
    public boolean isAccess(Slot slot) throws Throwable {
        return delegate.isAccess(slot);
    }

    @Override
    public ChainProxy setCurrChainName(String currentChainName) {
        delegate.setCurrChainName(currentChainName);
        return getSelf();
    }
}
