package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.InterceptorContext;

public interface ChainExecuteInterceptor extends Interceptor {

    Object initContext(InterceptorContext interceptorContext);

    default void beforeProcess(ChainInterceptorContext interceptorContext) {
    }

    default void onSuccess(ChainInterceptorContext interceptorContext) {
    }

    default void onError(ChainInterceptorContext interceptorContext) {
    }

    default void onFinally(ChainInterceptorContext interceptorContext) {
    }
}
