package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.ql.util.express.Operator;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.condition.ChainProxy;
import com.yomahub.liteflow.flow.element.condition.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的id的操作符,只有condition可加id
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class IdOperator extends Operator {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public Executable executeInner(Object[] objects) throws Exception {
        try {
            if (ArrayUtil.isEmpty(objects)) {
                throw new Exception();
            }

            if (objects.length != 2) {
                LOG.error("parameter error");
                throw new Exception("parameter error");
            }
            Object val = objects[0];
            Condition condition = null;
            ChainProxy chain = null;
            if (val instanceof Condition) {
                condition = (Condition) val;
            } else if (val instanceof Chain && !(val instanceof ChainProxy)) {
                chain = new ChainProxy((Chain) val);
            } else {
                String errMsg = StrUtil.format("The caller must be condition or chain item! it is: {}", objects[0]);
                LOG.error(errMsg);
                throw new Exception(errMsg);
            }
            val = objects[1];
            String id;
            if (val instanceof String) {
                id = val.toString();
            } else {
                String errMsg = StrUtil.format("the parameter must be String type! it is: {}", objects[1]);
                LOG.error(errMsg);
                throw new Exception(errMsg);
            }
            if (condition != null) {
                condition.setId(id);
                return condition;
            } else {
                chain.setId(id);
                return chain;
            }

        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing", e);
        }
    }
}
