package com.yomahub.liteflow.example.ext.flow.gyf;

import com.yomahub.liteflow.example.result.Result;
import com.yomahub.liteflow.parser.dsl.ChainDslScript;
import com.yomahub.liteflow.plugins.gyf.AddFuncOrOperationInterceptorContext;
import com.yomahub.liteflow.plugins.gyf.AddImportsInterceptorContext;
import com.yomahub.liteflow.plugins.gyf.GyfChainBuilderInterceptor;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExtFuncInterceptor implements GyfChainBuilderInterceptor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void addFuncOrOperation(AddFuncOrOperationInterceptorContext chainBuilderInterceptorContext) {
        chainBuilderInterceptorContext.addFunc("w_BizErr", new BusinessExceptionClosure(ChainDslScript.class, "w_BizErr"));
        chainBuilderInterceptorContext.addFunc("apply", new GyfApplyClosure(ChainDslScript.class, "apply", applicationContext));
    }

    @Override
    public void addImports(AddImportsInterceptorContext chainBuilderInterceptorContext) {
        chainBuilderInterceptorContext.addImportClass(Result.class);
        chainBuilderInterceptorContext.addImportClass(Level.class);
        chainBuilderInterceptorContext.addVariable("LEVEL_TRACE", Level.TRACE);
        chainBuilderInterceptorContext.addVariable("LEVEL_DEBUG", Level.DEBUG);
        chainBuilderInterceptorContext.addVariable("LEVEL_INFO", Level.INFO);
        chainBuilderInterceptorContext.addVariable("LEVEL_WARN", Level.WARN);
        chainBuilderInterceptorContext.addVariable("LEVEL_ERROR", Level.ERROR);
    }
}
