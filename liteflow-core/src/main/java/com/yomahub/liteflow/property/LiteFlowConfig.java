/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2021/3/18
 */
package com.yomahub.liteflow.property;

import cn.hutool.core.util.ObjectUtil;

import java.util.List;

/**
 * liteflow的配置实体类
 * 这个类中的属性为什么不用基本类型，而用包装类型呢
 * 是因为这个类是springboot和spring的最终参数获取器，考虑到spring的场景，有些参数不是必须配置。基本类型就会出现默认值的情况。
 * 所以为了要有null值出现，这里采用包装类型
 *
 * @author Bryan.Zhang
 */
public class LiteFlowConfig {

    /**
     * 是否启动liteflow自动装配
     */
    private Boolean enable;

    //slot的数量
    private Integer slotSize;

    private List<String> flowPaths;

    private LogProperties logConfig;

    private ExecutorProperties executorProperties;

    private MonitorProperties monitorProperties;

    private NodeComponentProperties nodeComponentProperties;



    public Boolean getEnable() {
        if (ObjectUtil.isNull(enable)) {
            return Boolean.TRUE;
        } else {
            return enable;
        }
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getSlotSize() {
        if (ObjectUtil.isNull(slotSize)) {
            return 1024;
        } else {
            return slotSize;
        }
    }

    public void setSlotSize(Integer slotSize) {
        this.slotSize = slotSize;
    }


    public LogProperties getLogConfig() {
        return logConfig;
    }

    public LiteFlowConfig setLogConfig(LogProperties logConfig) {
        this.logConfig = logConfig;
        return this;
    }

    public ExecutorProperties getExecutorProperties() {
        return executorProperties;
    }

    public LiteFlowConfig setExecutorProperties(ExecutorProperties executorProperties) {
        this.executorProperties = executorProperties;
        return this;
    }

    public MonitorProperties getMonitorProperties() {
        return monitorProperties;
    }

    public LiteFlowConfig setMonitorProperties(MonitorProperties monitorProperties) {
        this.monitorProperties = monitorProperties;
        return this;
    }

    public NodeComponentProperties getNodeComponentProperties() {
        return nodeComponentProperties;
    }

    public LiteFlowConfig setNodeComponentProperties(NodeComponentProperties nodeComponentProperties) {
        this.nodeComponentProperties = nodeComponentProperties;
        return this;
    }

    public List<String> getFlowPaths() {
        return flowPaths;
    }

    public LiteFlowConfig setFlowPaths(List<String> flowPaths) {
        this.flowPaths = flowPaths;
        return this;
    }
}
