package com.yomahub.liteflow.components;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.tenio.interstellar.context.spring.DataObjectAccessor;

public class SPELApplyComponent extends NodeComponent {
    private ApplicationContext applicationContext;
    private Expression expression;

    public SPELApplyComponent(ApplicationContext applicationContext, Expression expression) {
        this.applicationContext = applicationContext;
        this.expression = expression;
    }

    @Override
    public void process(Node node, Slot slot) throws Throwable {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.addPropertyAccessor(new MapAccessor());
        evaluationContext.addPropertyAccessor(new DataObjectAccessor());
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        evaluationContext.setVariables(slot.getContextBeanMap());
        evaluationContext.setVariables(slot.variables());
        expression.getValue(evaluationContext);
    }
}
