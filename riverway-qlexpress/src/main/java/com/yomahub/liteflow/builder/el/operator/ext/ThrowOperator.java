package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.components.ThrowComponent;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThrowOperator extends Operator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        if (objects.length < 1) {
            log.error("parameter error");
            throw new Exception("parameter error");
        }
        Object val = objects[0];
        if (!(val instanceof Throwable) && !(val instanceof ValueHandler)) {
            log.error("parameter 0 must be Throwable or ValueHandler");
            throw new Exception("parameter 0 must be Throwable or ValueHandler");
        }
        Node node = new Node();
        if (val instanceof Throwable) {
            node.setInstance(new ThrowComponent((Throwable) val));
        } else {
            node.setInstance(new ThrowComponent((ValueHandler) val));
        }
        return new NodeCondition(node);
    }
}
