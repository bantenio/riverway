package com.yomahub.liteflow.plugins;

import com.yomahub.liteflow.flow.element.Node;

public class InterceptorContext {
    private String chainName;
    private Object context;

    private boolean isFinally;

    private boolean hasError;

    private Exception error;

    public String getChainName() {
        return chainName;
    }

    public InterceptorContext setChainName(String chainName) {
        this.chainName = chainName;
        return this;
    }

    public <T> T getContext() {
        return (T) context;
    }

    public <T> InterceptorContext setContext(T context) {
        this.context = context;
        return this;
    }

    public boolean isFinally() {
        return isFinally;
    }

    public InterceptorContext setFinally(boolean aFinally) {
        isFinally = aFinally;
        return this;
    }

    public boolean isHasError() {
        return hasError;
    }

    public InterceptorContext setHasError(boolean hasError) {
        this.hasError = hasError;
        return this;
    }

    public Exception getError() {
        return error;
    }

    public InterceptorContext setError(Exception error) {
        this.error = error;
        return this;
    }

    @Override
    public String toString() {
        return "InterceptorContext{" +
                "chainName='" + chainName + '\'' +
                ", context=" + context +
                ", isFinally=" + isFinally +
                ", hasError=" + hasError +
                ", error=" + error +
                '}';
    }
}
