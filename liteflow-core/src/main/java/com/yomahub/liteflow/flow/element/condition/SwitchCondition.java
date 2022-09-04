package com.yomahub.liteflow.flow.element.condition;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.ConditionTypeEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.exception.NoSwitchTargetNodeException;
import com.yomahub.liteflow.exception.SwitchTargetCannotBePreOrFinallyException;
import com.yomahub.liteflow.exception.SwitchTypeErrorException;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.DataBus;
import com.yomahub.liteflow.slot.Slot;

import java.util.HashMap;
import java.util.Map;

/**
 * 条件Condition
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class SwitchCondition extends Condition {

    private final Map<String, Executable> targetMap = new HashMap<>();

    @Override
    public void execute(Integer slotIndex) throws Throwable {
        Executable executable = this.getSwitchNode();
        NodeComponent nodeComponent = null;
        if (executable instanceof Node) {
            Node ins = (Node) executable;
            nodeComponent = ins.getInstance();
        } else if (executable instanceof NodeCondition) {
            NodeCondition ins = (NodeCondition) executable;
            nodeComponent = ins.getNode().getInstance();
        }
        if (nodeComponent != null && ListUtil.toList(NodeTypeEnum.SWITCH, NodeTypeEnum.SWITCH_SCRIPT).contains(nodeComponent.getType())) {
            //先执行switch节点
            executable.setCurrChainName(this.getCurrChainName());
            executable.execute(slotIndex);

            //根据switch节点执行出来的结果选择
            Slot slot = DataBus.getSlot(slotIndex);
            String targetId = slot.getSwitchResult(nodeComponent.getClass().getName());
            if (StrUtil.isNotBlank(targetId)) {
                Executable targetExecutor = targetMap.get(targetId);
                if (ObjectUtil.isNotNull(targetExecutor)) {
                    //switch的目标不能是Pre节点或者Finally节点
                    if (targetExecutor instanceof PreCondition || targetExecutor instanceof FinallyCondition) {
                        String errorInfo = StrUtil.format("[{}]:switch component[{}] error, switch target node cannot be pre or finally", slot.getRequestId(), nodeComponent.getDisplayName());
                        throw new SwitchTargetCannotBePreOrFinallyException(errorInfo);
                    }
                    targetExecutor.setCurrChainName(this.getCurrChainName());
                    targetExecutor.execute(slotIndex);
                } else {
                    String errorInfo = StrUtil.format("[{}]:no target node find for the component[{}]", slot.getRequestId(), nodeComponent.getDisplayName());
                    throw new NoSwitchTargetNodeException(errorInfo);
                }
            }
        } else {
            throw new SwitchTypeErrorException("switch instance must be NodeSwitchComponent");
        }
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TYPE_SWITCH;
    }

    public void addTargetItem(Executable executable) {
        this.targetMap.put(executable.getExecuteName(), executable);
    }

    public void setSwitchNode(Node switchNode) {
        this.getExecutableList().add(switchNode);
    }

    public void setSwitchNode(NodeCondition switchNode) {
        this.getExecutableList().add(switchNode);
    }

    public Executable getSwitchNode() {
        return this.getExecutableList().get(0);
    }
}
