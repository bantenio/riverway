package com.yomahub.liteflow.plugins.gyf

import com.yomahub.liteflow.flow.FlowConfiguration
import com.yomahub.liteflow.flow.element.condition.NodeCondition
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.parser.dsl.ChainDslScript
import com.yomahub.liteflow.plugins.InterceptorContext

class AddFuncOrOperationInterceptorContext extends InterceptorContext<AddImportsInterceptorContext> {
    AddFuncOrOperationInterceptorContext(FlowConfiguration flowConfiguration) {
        super(flowConfiguration);
    }

    void addFunc(String name, Closure<?> closure) {
        ChainDslScript.metaClass."${name}" = { Object... args ->
            closure.delegate = delegate
            return closure.call(args)
        }
    }

    void addFuncForNodeCondition(String name, Closure<?> closure) {
        NodeCondition.metaClass."${name}" = { Object... args ->
            closure.delegate = delegate
            return closure.call(args)
        }
    }

    void addFuncForNode(String name, Closure<?> closure) {
        Node.metaClass."${name}" = { Object... args ->
            closure.delegate = delegate
            return closure.call(args)
        }
    }
}
