package com.yomahub.liteflow.plugins.gyf

import com.yomahub.liteflow.core.NodeComponent
import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.plugins.InterceptorContext
import org.codehaus.groovy.control.customizers.ImportCustomizer

class AddImportsInterceptorContext  extends InterceptorContext<AddImportsInterceptorContext> {
    private Binding binding

    private ImportCustomizer importCustomizer

    AddImportsInterceptorContext(FlowConfiguration flowConfiguration, Binding binding, ImportCustomizer importCustomizer) {
        super(flowConfiguration)
        this.binding = binding
        this.importCustomizer = importCustomizer
    }

    void addNodeComponent(String name, NodeComponent nodeComponent) {
        binding.setVariable(name, nodeComponent)
    }

    void addVariable(String name, Object var) {
        binding.setVariable(name, var)
    }

    void addImportClass(Class<?> clazz) {
        if (clazz == null) {
            return
        }
        importCustomizer.addImports(clazz.getName())
    }
}
