package com.yomahub.liteflow.plugins.el;

import com.yomahub.liteflow.plugins.Interceptor;

public interface ChainBuilderInterceptor extends Interceptor {
    default void addFuncOrOperation(AddFuncOrOperationInterceptorContext chainBuilderInterceptorContext) {

    }

    default void addImports(AddImportsInterceptorContext chainBuilderInterceptorContext) {

    }
}
