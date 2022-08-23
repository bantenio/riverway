package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PutInMapOperator extends Operator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public Object executeInner(Object[] objects) throws Exception {
        if (objects.length < 3) {
            log.error("parameter error");
            throw new Exception("parameter error");
        }
        Map<Object, Object> target;
        if (!(objects[0] instanceof Map)) {
            log.error("The caller must be Map item!");
            throw new Exception("The caller must be Map item!");
        } else {
            target = (Map<Object, Object>) objects[0];
        }
        target.put(objects[1], objects[2]);
        return target;
    }
}
