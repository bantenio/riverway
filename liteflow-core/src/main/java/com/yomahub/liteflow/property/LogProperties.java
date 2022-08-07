package com.yomahub.liteflow.property;

import cn.hutool.core.util.ObjectUtil;

public class LogProperties {

    //是否打印监控log
    private Boolean enableLog = Boolean.FALSE;

    //是否打印liteflow banner
    private Boolean printBanner = Boolean.FALSE;

    //是否打印执行中的日志
    private Boolean printExecutionLog = Boolean.TRUE;


    public Boolean getEnableLog() {
        if (ObjectUtil.isNull(enableLog)) {
            return Boolean.FALSE;
        } else {
            return enableLog;
        }
    }

    public void setEnableLog(Boolean enableLog) {
        this.enableLog = enableLog;
    }

    public Boolean getPrintBanner() {
        return printBanner;
    }

    public LogProperties setPrintBanner(Boolean printBanner) {
        this.printBanner = printBanner;
        return this;
    }

    public Boolean getPrintExecutionLog() {
        return printExecutionLog;
    }

    public LogProperties setPrintExecutionLog(Boolean printExecutionLog) {
        this.printExecutionLog = printExecutionLog;
        return this;
    }

    @Override
    public String toString() {
        return "LogProperties{" +
                "enableLog=" + enableLog +
                ", printBanner=" + printBanner +
                ", printExecutionLog=" + printExecutionLog +
                '}';
    }
}
