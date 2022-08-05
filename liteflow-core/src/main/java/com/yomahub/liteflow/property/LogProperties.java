package com.yomahub.liteflow.property;

public class LogProperties {

    //是否打印liteflow banner
    private Boolean printBanner = Boolean.FALSE;

    //是否打印执行中的日志
    private Boolean printExecutionLog = Boolean.TRUE;

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
        return "LogConfig{" +
                "printBanner=" + printBanner +
                ", printExecutionLog=" + printExecutionLog +
                '}';
    }
}
