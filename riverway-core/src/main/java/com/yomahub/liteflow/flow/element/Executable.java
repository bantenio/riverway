package com.yomahub.liteflow.flow.element;

import com.yomahub.liteflow.enums.ExecuteTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;

/**
 * 可执行器接口
 * 目前实现这个接口的有3个，Chain，Condition，Node
 *
 * @author Bryan.Zhang
 */
public interface Executable {

    void execute(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable;

    default boolean isAccess(Integer slotIndex) throws Throwable {
        return true;
    }

    ExecuteTypeEnum getExecuteType();

    String getExecuteName();

    default void setCurrChainName(String currentChainName) {

    }
}
