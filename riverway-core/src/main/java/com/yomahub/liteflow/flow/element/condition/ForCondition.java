package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.components.MutableValueHandler;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.exception.NoForNodeException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

public class ForCondition extends LoopCondition<ForCondition> {
    private NodeCondition forNode;

    private String indexVariableName;

    @Override
    public void process(Slot slot, FlowConfiguration flowConfiguration) throws Throwable {
        if (ObjectUtil.isNull(forNode) || !forNode.hasResult()) {
            String errorInfo = CharSequenceUtil.format("[{}]:no for-node found", slot.getRequestId());
            throw new NoForNodeException(errorInfo);
        }

        //获得要循环的可执行对象
        Executable<? extends Executable<?>> executableItem = this.getDoExecutor();
        NodeCondition breakNode = getBreakNode();
        boolean hasBreak = ObjectUtil.isNotNull(breakNode);

        int forCount = getIntegerResult(forNode, slot, flowConfiguration);
        MutableValueHandler indexValueHandler = null;
        boolean hasIndexVariable = CharSequenceUtil.isNotBlank(indexVariableName);
        //循环执行
        for (int i = 0; i < forCount; i++) {
            if (hasIndexVariable && executableItem instanceof NodeCondition) {
                if (indexValueHandler == null) {
                    indexValueHandler = new MutableValueHandler();
                }
                indexValueHandler.setValue(i);
                ((NodeCondition) executableItem).addSwapHandler(indexVariableName, indexValueHandler);
            }
            executableItem.execute(slot, flowConfiguration);
            //如果break组件不为空，则去执行
            if (hasBreak && getBooleanResult(breakNode, slot, flowConfiguration)) {
                break;
            }
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_FOR;
    }

    public Executable<? extends Executable<?>> getDoExecutor() {
        return this.getExecutableList().get(0);
    }

    public NodeCondition getForNode() {
        return forNode;
    }

    public ForCondition setForNode(NodeCondition forNode) {
        this.forNode = forNode;
        return this;
    }

    public ForCondition setForNode(Node forNode) {
        this.forNode = new NodeCondition(forNode);
        return this;
    }

    public ForCondition indexVariableName(String key) {
        this.indexVariableName = key;
        return this;
    }
}
