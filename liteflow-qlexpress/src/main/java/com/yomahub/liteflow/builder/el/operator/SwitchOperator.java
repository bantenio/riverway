package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.util.ArrayUtil;
import com.ql.util.express.Operator;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.flow.element.condition.SwitchCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的SWITCH的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class SwitchOperator extends Operator {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public SwitchCondition executeInner(Object[] objects) throws Exception {
        try {
            if (ArrayUtil.isEmpty(objects)) {
                throw new Exception();
            }

            if (objects.length != 1) {
                LOG.error("parameter error");
                throw new Exception("parameter error");
            }

            Node switchNode;
            if (objects[0] instanceof Node) {
                switchNode = (Node) objects[0];
            } else if (objects[0] instanceof NodeCondition) {
                switchNode = ((NodeCondition) objects[0]).getNode();
            } else {
                LOG.error("The caller must be Node or NodeCondition item!");
                throw new Exception("The caller must be Node or NodeCondition item!");
            }

            SwitchCondition switchCondition = new SwitchCondition();
            switchCondition.setSwitchNode(switchNode);

            return switchCondition;
        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing");
        }
    }
}
