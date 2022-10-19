/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.flow.element.condition;

import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.slot.Slot;

/**
 * 前置Condition
 * @author Bryan.Zhang
 * @since 2.6.4
 */
public class PreCondition extends Condition<PreCondition> {

    @Override
    public void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        for (Executable<? extends Executable<?>> executableItem : this.getExecutableList()) {
            executableItem.setCurrChainName(this.getCurrChainName());
            executableItem.execute(slot, flowConfiguration);
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_PRE;
    }
}
