package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.components.MutableValueHandler;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.NoWhileNodeException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

public class WhileCondition extends LoopCondition<WhileCondition> {
    private NodeCondition whileNode;

    private String indexVariableName;

    @Override
    public void process(Integer slotIndex, FlowConfiguration flowConfiguration) throws Throwable {
        Slot slot = DataBus.getSlot(slotIndex);
        if (ObjectUtil.isNull(whileNode) || !whileNode.hasResult()) {
            String errorInfo = CharSequenceUtil.format("[{}]:no while-node found", slot.getRequestId());
            throw new NoWhileNodeException(errorInfo);
        }

        //获得要循环的可执行对象
        Executable<? extends Executable<?>> executableItem = this.getDoExecutor();
        NodeCondition breakNode = getBreakNode();
        boolean hasBreak = ObjectUtil.isNotNull(breakNode);

        MutableValueHandler indexValueHandler = null;
        boolean hasIndexVariable = CharSequenceUtil.isNotBlank(indexVariableName);
        int loopIndex = 1;
        //循环执行
        while (getBooleanResult(whileNode, slotIndex, flowConfiguration)) {
            if (hasIndexVariable && executableItem instanceof NodeCondition) {
                if (indexValueHandler == null) {
                    indexValueHandler = new MutableValueHandler();
                }
                indexValueHandler.setValue(loopIndex);
                ((NodeCondition) executableItem).addSwapHandler(indexVariableName, indexValueHandler);
            }
            executableItem.execute(slotIndex, flowConfiguration);
            //如果break组件不为空，则去执行
            if (hasBreak && getBooleanResult(breakNode, slotIndex, flowConfiguration)) {
                break;
            }
            loopIndex++;
        }
    }

    public Executable<? extends Executable<?>> getDoExecutor() {
        return this.getExecutableList().get(0);
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_WHILE;
    }

    public NodeCondition getWhileNode() {
        return whileNode;
    }

    public WhileCondition setWhileNode(NodeCondition whileNode) {
        this.whileNode = whileNode;
        return this;
    }

    public WhileCondition setWhileNode(Node whileNode) {
        this.whileNode = new NodeCondition(whileNode);
        return this;
    }

    public WhileCondition indexVariableName(String key) {
        this.indexVariableName = key;
        return this;
    }
}
