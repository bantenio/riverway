/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.slot;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.exception.LiteFlowException;
import com.yomahub.liteflow.exception.NoAvailableSlotException;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.property.LiteFlowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 数据BUS，主要用来管理Slot，用以分配和回收
 *
 * @author Bryan.Zhang
 */
public class DataBus {

    private static final Logger log = LoggerFactory.getLogger(DataBus.class);

    public static AtomicInteger OCCUPY_COUNT = new AtomicInteger(0);

    //这里为什么采用ConcurrentHashMap作为slot存放的容器？
    //因为ConcurrentHashMap的随机取值复杂度也和数组一样为O(1)，并且没有并发问题，还有自动扩容的功能
    //用数组的话，扩容涉及copy，线程安全问题还要自己处理
    private static ConcurrentHashMap<Integer, Slot> SLOTS;

    private static ConcurrentLinkedQueue<Integer> QUEUE;

    //当前slot的下标index的最大值
    private static Integer currentIndexMaxValue;

    //这里原先版本中是static块，现在改成init静态方法，由FlowExecutor中的init去调用
    //这样的改动对项目来说没有什么实际意义，但是在单元测试中，却有意义。
    //因为单元测试中所有的一起跑，jvm是不退出的，所以如果是static块的话，跑多个testsuite只会执行一次。
    //而由FlowExecutor中的init去调用，是会被执行多次的。保证了每个单元测试都能初始化一遍
    public static void init(LiteFlowConfig liteflowConfig) {
        if (MapUtil.isEmpty(SLOTS)) {
            currentIndexMaxValue = liteflowConfig.getSlotSize();

            SLOTS = new ConcurrentHashMap<>();
            QUEUE = IntStream.range(0, currentIndexMaxValue).boxed().collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        }
    }

    public static Slot offerSlot(Map<String, Object> params, FlowConfiguration flowConfiguration) {
        Integer slotIndex;
        try {

            //这里有没有并发问题？
            //没有，因为QUEUE的类型为ConcurrentLinkedQueue，并发情况下，每次取到的index不会相同
            //当然前提是QUEUE里面的值不会重复，但是这个是由其他机制来保证的
            slotIndex = QUEUE.poll();
            if (ObjectUtil.isNull(slotIndex)) {
                //只有在扩容的时候需要用到synchronized重量级锁
                //扩一次容，增强原来size的0.75，因为初始slot容量为1024，从某种层面来说，即便并发很大。但是扩容的次数不会很多。
                //因为单个机器的tps上限总归是有一个极限的，不可能无限制的增长。
                synchronized (DataBus.class) {
                    //在扩容的一刹那，去竞争这个锁的线程还是有一些，所以获得这个锁的线程这里要再次取一次。如果为null，再真正扩容
                    slotIndex = QUEUE.poll();
                    if (ObjectUtil.isNull(slotIndex)) {
                        int nextMaxIndex = (int) Math.round(currentIndexMaxValue * 1.75);
                        QUEUE.addAll(IntStream.range(currentIndexMaxValue, nextMaxIndex).boxed().collect(Collectors.toCollection(ConcurrentLinkedQueue::new)));
                        currentIndexMaxValue = nextMaxIndex;
                        //扩容好，从队列里再取出扩容好的index
                        slotIndex = QUEUE.poll();
                    }
                }
            }

            if (ObjectUtil.isNotNull(slotIndex)) {
                Slot slot = new DefaultSlot(flowConfiguration, params, slotIndex);
                SLOTS.put(slotIndex, slot);
                OCCUPY_COUNT.incrementAndGet();
                return slot;
            }
        } catch (Exception e) {
            throw new NoAvailableSlotException("offer slot error", e);
        }
        throw new NoAvailableSlotException(StrUtil.format("the slot[{}] is not exist", slotIndex));
    }

    public static Slot getSlot(int slotIndex) {
        return SLOTS.get(slotIndex);
    }

    public static void releaseSlot(int slotIndex) {
        if (ObjectUtil.isNotNull(SLOTS.get(slotIndex))) {
            log.info("[{}]:slot[{}] released", SLOTS.get(slotIndex).getRequestId(), slotIndex);
            SLOTS.remove(slotIndex);
            QUEUE.add(slotIndex);
            OCCUPY_COUNT.decrementAndGet();
        } else {
            log.warn("slot[{}] already has been released", slotIndex);
        }
    }
}
