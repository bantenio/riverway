package com.yomahub.liteflow.builder.el.operator;

import com.ql.util.express.Operator;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.condition.ThenCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的THEN的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class ThenOperator extends Operator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        try {
            if (objects.length <= 0) {
                log.error("parameter error");
                throw new Exception("parameter error");
            }

            ThenCondition thenCondition = new ThenCondition();
            for (Object obj : objects) {
                if (obj instanceof Executable) {
                    thenCondition.addExecutable((Executable) obj);
                } else {
                    log.error("parameter must be executable item!");
                    throw new Exception("parameter must be executable item!");
                }
            }
            return thenCondition;
        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing", e);
        }
    }
}