package com.yomahub.liteflow.plugins.el;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.instruction.op.OperatorBase;
import com.yomahub.liteflow.flow.FlowConfiguration;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import com.yomahub.liteflow.plugins.InterceptorContext;

public class AddFuncOrOperationInterceptorContext extends InterceptorContext<AddFuncOrOperationInterceptorContext> {
    private final ExpressRunner expressRunner;

    public AddFuncOrOperationInterceptorContext(FlowConfiguration flowConfiguration,
                                                ExpressRunner expressRunner) {
        super(flowConfiguration);
        this.expressRunner = expressRunner;
    }

    public AddFuncOrOperationInterceptorContext addFunc(String name, OperatorBase op) {
        expressRunner.addFunction(name, op);
        return this;
    }

    public AddFuncOrOperationInterceptorContext addFuncForNodeCondition(String name, OperatorBase op) {
        expressRunner.addFunctionAndClassMethod(name, NodeCondition.class, op);
        return this;
    }

    public AddFuncOrOperationInterceptorContext addFuncForNode(String name, OperatorBase op) {
        expressRunner.addFunctionAndClassMethod(name, Node.class, op);
        return this;
    }

}
