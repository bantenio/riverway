package com.yomahub.liteflow.plugins.el;

import cn.hutool.core.collection.CollUtil;
import com.ql.util.express.DefaultContext;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.plugins.InterceptorContext;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class AddImportsInterceptorContext extends InterceptorContext<AddImportsInterceptorContext> {
    private final DefaultContext<String, Object> context;

    private final Set<String> imports = new LinkedHashSet<>();

    public AddImportsInterceptorContext(DefaultContext<String, Object> context) {
        this.context = context;
    }

    public void addNodeComponent(String name, NodeComponent nodeComponent) {
        context.put(name, nodeComponent);
    }

    public void addVariable(String name, Object var) {
        context.put(name, var);
    }

    public void addImportClass(Class clazz) {
        if (clazz == null) {
            return;
        }
        imports.add("import " + clazz.getName() + ';');
    }

    public Collection<String> getImports() {
        return CollUtil.unmodifiable(imports);
    }
}
