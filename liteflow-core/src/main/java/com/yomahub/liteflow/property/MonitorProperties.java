package com.yomahub.liteflow.property;

import cn.hutool.core.util.ObjectUtil;

public class MonitorProperties {

    //监控存储信息最大队列数量
    private Integer queueLimit;

    //延迟多少秒打印
    private Long delay;

    //每隔多少秒打印
    private Long period;



    public Integer getQueueLimit() {
        if (ObjectUtil.isNull(queueLimit)) {
            return 200;
        } else {
            return queueLimit;
        }
    }

    public void setQueueLimit(Integer queueLimit) {
        this.queueLimit = queueLimit;
    }

    public Long getDelay() {
        if (ObjectUtil.isNull(delay)) {
            return 300000L;
        } else {
            return delay;
        }
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getPeriod() {
        if (ObjectUtil.isNull(period)) {
            return 300000L;
        } else {
            return period;
        }
    }
}
