package com.yomahub.liteflow.builder.el.operator.ext;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.ql.util.express.Operator;
import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAround;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LogOperator extends Operator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object executeInner(Object[] objects) throws Exception {
        if (objects.length < 3) {
            log.error("parameter error");
            throw new Exception("parameter error");
        }
        int opIdx = 0;
        int paramOffset = 1;
        Object var = objects[opIdx];
        if (!(var instanceof NodeCondition)) {
            log.error("The caller must be NodeCondition item!");
            throw new Exception("The caller must be NodeCondition item!");
        }
        NodeCondition nodeCondition = (NodeCondition) var;

        opIdx++;
        var = objects[opIdx];
        if (!(var instanceof Level)) {
            String msg = StrUtil.format("parameter {} must be org.slf4j.event.Level", opIdx - paramOffset);
            log.error(msg);
            throw new Exception(msg);
        }
        Level level = (Level) var;

        opIdx++;
        var = objects[opIdx];
        if (!(var instanceof String)) {
            String msg = StrUtil.format("parameter {} must be String", opIdx - paramOffset);
            log.error(msg);
            throw new Exception(msg);
        }
        String message = (String) var;


        Object[] args = ArrayUtil.sub(objects, 1, -1);
        for (int idx = 0; idx < args.length; idx++) {
            opIdx++;
            Object arg = args[idx];
            if (!(arg instanceof ValueHandler) && !(arg instanceof String)) {
                String msg = StrUtil.format("parameter {} must be String or ValueHandler", opIdx - paramOffset);
                log.error(msg);
                throw new Exception(msg);
            }
        }
        nodeCondition.addNodeAroundCondition(new LogNodeAround(message, args, level));
        return nodeCondition;
    }

    public static class LogNodeAround implements NodeAround {
        private static final Logger log = LoggerFactory.getLogger(LogNodeAround.class);

        private final String message;

        private final Object[] args;

        private final Level level;

        public LogNodeAround(String message, Object[] args, Level level) {
            this.message = message;
            this.args = args;
            this.level = level;
        }

        @Override
        public void before(NodeCondition nodeCondition, Node node, Slot slot) throws Throwable {
            Object[] tmpArgs = new Object[args.length];
            for (int idx = 0; idx < args.length; idx++) {
                Object arg = args[idx];
                if (arg instanceof ValueHandler) {
                    tmpArgs[idx] = ((ValueHandler) arg).getValue(slot, node);
                } else if (arg instanceof String) {
                    tmpArgs[idx] = slot.getVariable((String) arg);
                }
            }
            if (log.isTraceEnabled() && Level.TRACE.equals(level)) {
                log.trace(message, tmpArgs);
            } else if (log.isDebugEnabled() && Level.DEBUG.equals(level)) {
                log.debug(message, tmpArgs);
            } else if (log.isInfoEnabled() && Level.INFO.equals(level)) {
                log.info(message, tmpArgs);
            } else if (log.isWarnEnabled() && Level.WARN.equals(level)) {
                log.warn(message, tmpArgs);
            } else if (log.isErrorEnabled() && Level.ERROR.equals(level)) {
                log.error(message, tmpArgs);
            }
        }
    }
}
