package com.yomahub.liteflow.flow.element;

import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.slot.Slot;

/**
 * 可执行器接口
 * 目前实现这个接口的有3个，Chain，Condition，Node
 *
 * @author Bryan.Zhang
 */
public interface Executable<T extends Executable<T>> {

    T getSelf();

    default Object execute(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        if (hasResult()) {
            return processWithResult(slot, flowConfiguration);
        } else {
            process(slot, flowConfiguration);
            return null;
        }
    }

    void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable;

    default Object processWithResult(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        return null;
    }


    default boolean isAccess(Slot slot) throws Throwable {
        return true;
    }

    ExecuteTypeEnum getExecuteType();

    String getExecuteName();

    default T setCurrChainName(String currentChainName) {
        return getSelf();
    }

    default boolean hasResult() {
        return false;
    }
}
