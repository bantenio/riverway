package com.yomahub.liteflow.builder.el.operator;

import cn.hutool.core.text.CharSequenceUtil;
import com.ql.util.express.Operator;
import com.yomahub.liteflow.exception.ELParseException;
import com.yomahub.liteflow.flow.element.Executable;
import com.yomahub.liteflow.flow.element.condition.WhenCondition;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EL规则中的WHEN的操作符
 *
 * @author Bryan.Zhang
 * @since 2.8.0
 */
public class WhenOperator extends Operator {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public WhenCondition executeInner(Object[] objects) throws Exception {
        try {
            if (objects.length <= 0) {
                LOG.error("parameter error");
                throw new Exception("parameter error");
            }
            int opIdx = -1;
            WhenCondition whenCondition = new WhenCondition();
            for (Object obj : objects) {
                opIdx++;
                if (obj instanceof Executable) {
                    whenCondition.addExecutable((Executable) obj);
                } else {
                    throw new Exception(CharSequenceUtil.format("parameter {} must be Executable instance!", opIdx));
                }
            }
            return whenCondition;
        } catch (Exception e) {
            throw new ELParseException("errors occurred in EL parsing", e);
        }
    }
}
