package com.yomahub.liteflow.builder.el.operator;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodePropertyOperator extends Operator {

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
                throw new Exception();
            } else {
                target = (NodeCondition) objects[0];
            }

            if (!(objects[1] instanceof String)) {
                log.error("parameter 0 must be String");
                throw new Exception();
            }
            String paramName = objects[1].toString();
            target.addProperty(paramName, objects[2]);

            return target;
        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing", e);
        }
    }
}
