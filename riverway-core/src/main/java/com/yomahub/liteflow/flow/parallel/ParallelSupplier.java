package com.yomahub.liteflow.flow.parallel;

import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * 并行异步worker对象，提供给CompletableFuture用
 *
 * @author Bryan.Zhang
 * @since 2.6.4
 */
public class ParallelSupplier implements Supplier<WhenFutureObj> {

    private static final Logger LOG = LoggerFactory.getLogger(ParallelSupplier.class);

    private final Executable executableItem;

    private final String currChainName;

    private final Integer slotIndex;

    private final FlowConfiguration flowConfiguration;

    public ParallelSupplier(Executable executableItem, String currChainName, Integer slotIndex, FlowConfiguration flowConfiguration) {
        this.executableItem = executableItem;
        this.currChainName = currChainName;
        this.slotIndex = slotIndex;
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public WhenFutureObj get() {
        try {
            executableItem.setCurrChainName(currChainName);
            executableItem.execute(slotIndex, flowConfiguration);
            return WhenFutureObj.success(executableItem.getExecuteName());
        } catch (Throwable e) {
            return WhenFutureObj.fail(executableItem.getExecuteName(), e);
        }
    }
}
