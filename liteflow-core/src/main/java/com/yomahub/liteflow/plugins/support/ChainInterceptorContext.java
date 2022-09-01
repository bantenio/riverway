package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.plugins.InterceptorContext;

public class ChainInterceptorContext<Sub extends ChainInterceptorContext<Sub>> extends InterceptorContext<Sub> {
    private String chainName;

    private boolean isFinally;

    private boolean hasError;

    private Exception error;
    private Object context;

    public <T> T getContext() {
        return (T) context;
    }

    public Sub setContext(Object context) {
        this.context = context;
        return (Sub) this;
    }


    public String getChainName() {
        return chainName;
    }

    public Sub setChainName(String chainName) {
        this.chainName = chainName;
        return (Sub) this;
    }

    public boolean isFinally() {
        return isFinally;
    }

    public Sub setFinally(boolean aFinally) {
        isFinally = aFinally;
        return (Sub) this;
    }

    public boolean isHasError() {
        return hasError;
    }

    public Sub setHasError(boolean hasError) {
        this.hasError = hasError;
        return (Sub) this;
    }

    public Exception getError() {
        return error;
    }

    public Sub setError(Exception error) {
        this.error = error;
        return (Sub) this;
    }

    @Override
    public String toString() {
        return "ChainInterceptorContext{" +
                "chainName='" + chainName + '\'' +
                ", isFinally=" + isFinally +
                ", hasError=" + hasError +
                ", error=" + error +
                ", context=" + context +
                "} ";
    }
}
