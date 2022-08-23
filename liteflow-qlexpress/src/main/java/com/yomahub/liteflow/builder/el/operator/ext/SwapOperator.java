package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwapOperator extends Operator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        try {
            if (objects.length < 3) {
                log.error("parameter error");
                throw new Exception();
            }
            NodeCondition target;
            if (!(objects[0] instanceof NodeCondition)) {
                log.error("The caller must be NodeCondition item!");
                throw new Exception("The caller must be NodeCondition item!");
            } else {
                target = (NodeCondition) objects[0];
            }

            if (!(objects[1] instanceof String) && !(objects[1] instanceof ValueHandler)) {
                log.error("parameter 0 must be String or ValueHandler");
                throw new Exception("parameter 0 must be String or ValueHandler");
            }
            String paramName = objects[1].toString();
            target.addSwapHandler(paramName, objects[2]);

            return target;
        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing", e);
        }
    }
}
