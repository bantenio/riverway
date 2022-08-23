package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.components.RefValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefOperator extends Operator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        if (objects.length < 1) {
            log.error("parameter error");
            throw new Exception("parameter error");
        }

        if (!(objects[0] instanceof String)) {
            log.error("parameter 0 must be String");
            throw new Exception("parameter 0 must be String");
        }
        String type = "ql";
        if (objects.length >= 2) {
            if(!(objects[1] instanceof String)) {
                log.error("parameter 1 must be String");
                throw new Exception("parameter 1 must be String");
            }
            type = objects[1].toString();
        }
        return new RefValueHandler(objects[0].toString(), type);
    }
}