package com.yomahub.liteflow.components.ext;

import com.yomahub.liteflow.components.ValueHandler;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.flow.element.condition.ext.NodeAround;
import com.yomahub.liteflow.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LogNodeAround implements NodeAround {
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
        if (log.isTraceEnabled() && Level.TRACE == level) {
            log.trace(message, tmpArgs);
        } else if (log.isDebugEnabled() && Level.DEBUG == level) {
            log.debug(message, tmpArgs);
        } else if (log.isInfoEnabled() && Level.INFO == level) {
            log.info(message, tmpArgs);
        } else if (log.isWarnEnabled() && Level.WARN == level) {
            log.warn(message, tmpArgs);
        } else if (log.isErrorEnabled() && Level.ERROR == level) {
            log.error(message, tmpArgs);
        }
    }
}