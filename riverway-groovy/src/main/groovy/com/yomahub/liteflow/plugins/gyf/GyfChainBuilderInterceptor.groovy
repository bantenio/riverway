package com.yomahub.liteflow.plugins.gyf

import com.yomahub.liteflow.plugins.Interceptor

interface GyfChainBuilderInterceptor extends Interceptor {
    default void addFuncOrOperation(AddFuncOrOperationInterceptorContext chainBuilderInterceptorContext) {

    }

    default void addImports(AddImportsInterceptorContext chainBuilderInterceptorContext) {

    }
}
