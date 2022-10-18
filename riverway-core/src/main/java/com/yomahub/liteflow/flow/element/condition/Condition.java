/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.flow.element.condition;

import com.yomahub.liteflow.common.LocalDefaultFlowConstant;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.element.Executable;

import java.util.ArrayList;
import java.util.List;

/**
 * Condition的抽象类
 *
 * @author Bryan.Zhang
 */
public abstract class Condition<T extends Condition<T>> implements Executable<T> {

    private String id;

    @Override
    public T getSelf() {
        return (T) this;
    }

    //可执行元素的集合
    private List<Executable<? extends Executable<?>>> executableList = new ArrayList<>();

    //只在when类型下有效，以区分当when调用链调用失败时是否继续往下执行 默认false不继续执行
    private boolean errorResume = false;

    //只在when类型下有效，用于不同node进行同组合并，相同的组会进行合并，不同的组不会进行合并
    private String group = LocalDefaultFlowConstant.DEFAULT;

    //只在when类型下有效，为true的话说明在多个并行节点下，任意一个成功，整个when就成功
    private boolean any = false;

    // when单独的线程池名称
    private String threadExecutorName;

    //当前所在的ChainName
    //如果对于子流程来说，那这个就是子流程所在的Chain
    private String currChainName;

    @Override
    public ExecuteTypeEnum getExecuteType() {
        return ExecuteTypeEnum.CONDITION;
    }

    @Override
    public String getExecuteName() {
        return this.id;
    }

    public List<Executable<? extends Executable<?>>> getExecutableList() {
        return executableList;
    }

    public T setExecutableList(List<Executable<? extends Executable<?>>> executableList) {
        this.executableList = executableList;
        return getSelf();
    }

    public T addExecutable(Executable<? extends Executable<?>> executable) {
        this.executableList.add(executable);
        return getSelf();
    }

    public boolean isErrorResume() {
        return errorResume;
    }

    public T setErrorResume(boolean errorResume) {
        this.errorResume = errorResume;
        return getSelf();
    }

    public T errorResume(boolean errorResume) {
        this.errorResume = errorResume;
        return getSelf();
    }

    public T ignoreError(boolean errorResume) {
        this.errorResume = errorResume;
        return getSelf();
    }

    public String getGroup() {
        return group;
    }

    public T setGroup(String group) {
        this.group = group;
        return getSelf();
    }

    public T group(String group) {
        this.group = group;
        return getSelf();
    }

    public abstract ConditionTypeEnum getConditionType();

    public boolean isAny() {
        return any;
    }

    public T setAny(boolean any) {
        this.any = any;
        return getSelf();
    }

    public T any(boolean any) {
        this.any = any;
        return getSelf();
    }

    public String getThreadExecutorName() {
        return threadExecutorName;
    }

    public T setThreadExecutorName(String threadExecutorName) {
        this.threadExecutorName = threadExecutorName;
        return getSelf();
    }

    public T threadPool(String threadExecutorName) {
        this.threadExecutorName = threadExecutorName;
        return getSelf();
    }

    public String getId() {
        return id;
    }

    public T setId(String id) {
        this.id = id;
        return getSelf();
    }

    public T id(String id) {
        this.id = id;
        return getSelf();
    }

    public String getCurrChainName() {
        return currChainName;
    }

    @Override
    public T setCurrChainName(String currChainName) {
        this.currChainName = currChainName;
        return getSelf();
    }
}
