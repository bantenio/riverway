package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.components.ThrowComponent;
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
        if (!(objects[0] instanceof Throwable)) {
            log.error("parameter 0 must be Throwable");
            throw new Exception("parameter 0 must be Throwable");
        }
        return new ThrowComponent((Throwable) objects[0]);
    }
}
