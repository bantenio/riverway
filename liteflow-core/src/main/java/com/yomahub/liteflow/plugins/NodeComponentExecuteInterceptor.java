package com.yomahub.liteflow.plugins;

public interface NodeComponentExecuteInterceptor extends Interceptor {

    void beforeProcess(NodeComponentInterceptorContext interceptorContext);

    void onSuccess(NodeComponentInterceptorContext interceptorContext);

    void onError(NodeComponentInterceptorContext interceptorContext);

    void onFinally(NodeComponentInterceptorContext interceptorContext);
}
