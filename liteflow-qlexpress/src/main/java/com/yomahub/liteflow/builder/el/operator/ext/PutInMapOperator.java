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
            throw new Exception();
        }
        Map<Object, Object> target;
        if (!(objects[0] instanceof Map)) {
            log.error("The caller must be Map item!");
            throw new Exception();
        } else {
            target = (Map<Object, Object>) objects[0];
        }

        if (!(objects[1] instanceof String) || !(objects[2] instanceof String)) {
            log.error("parameter 0 and 1 must be String");
            throw new Exception();
        }
        String paramName = objects[1].toString();
        target.put(paramName, objects[2]);
        return target;
    }
}
