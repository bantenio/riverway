package com.yomahub.liteflow.plugins.support;

import com.yomahub.liteflow.plugins.Interceptor;
import com.yomahub.liteflow.plugins.InterceptorContext;

public interface NodeComponentExecuteInterceptor extends Interceptor {

    Object initContext(InterceptorContext interceptorContext);

    void beforeProcess(NodeComponentInterceptorContext interceptorContext);

    void onSuccess(NodeComponentInterceptorContext interceptorContext);

    void onError(NodeComponentInterceptorContext interceptorContext);

    void onFinally(NodeComponentInterceptorContext interceptorContext);
}
