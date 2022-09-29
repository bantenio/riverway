package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.slot.Slot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.tenio.interstellar.context.spring.DataObjectAccessor;

public class SPELAcceptComponent extends OutResultComponent {
    private ApplicationContext applicationContext;
    private Expression expression;

    public SPELAcceptComponent(ApplicationContext applicationContext, Expression expression) {
        this.applicationContext = applicationContext;
        this.expression = expression;
    }

    @Override
    public Object innerProcess(Node node, Slot slot) throws Throwable {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.addPropertyAccessor(new MapAccessor());
        evaluationContext.addPropertyAccessor(new DataObjectAccessor());
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        evaluationContext.setVariables(slot.getContextBeanMap());
        evaluationContext.setVariables(slot.variables());
        return expression.getValue(evaluationContext);
    }
}
