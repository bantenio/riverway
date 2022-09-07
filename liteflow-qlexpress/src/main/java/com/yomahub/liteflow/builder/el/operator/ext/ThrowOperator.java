package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.components.ThrowComponent;
import com.yomahub.liteflow.components.ValueHandler;
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
        if (val instanceof Throwable) {
            return new ThrowComponent((Throwable) val);
        } else {
            return new ThrowComponent((ValueHandler) val);
        }
    }
}
