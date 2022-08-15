package com.yomahub.liteflow.plugins;

public interface ChainExecuteInterceptor {

    default void beforeProcess(InterceptorContext interceptorContext) {
    }

    default void onSuccess(InterceptorContext interceptorContext) {
    }

    default void onError(InterceptorContext interceptorContext) {
    }

    default void onFinally(InterceptorContext interceptorContext) {
    }
}
