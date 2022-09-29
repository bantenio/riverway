package com.yomahub.liteflow.components;

import com.yomahub.liteflow.flow.element.Node;
import com.yomahub.liteflow.flow.element.condition.NodeCondition;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.Expression;

public class SPELComponentBuilder {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SPELComponentBuilder.applicationContext = applicationContext;
    }

    public static NodeCondition createObtainApplyExpression(Expression expression) {
        SPELApplyComponent spelApplyComponent = new SPELApplyComponent(applicationContext, expression);
        Node node = new Node();
        node.setInstance(spelApplyComponent);
        node.setName("spelApplyComponent");
        return new NodeCondition(node);
    }

    public static NodeCondition createObtainAcceptExpression(Expression expression) {
        SPELAcceptComponent spelAcceptComponent = new SPELAcceptComponent(applicationContext, expression);
        Node node = new Node();
        node.setInstance(spelAcceptComponent);
        node.setName("spelApplyComponent");
        return new NodeCondition(node);
    }
}
