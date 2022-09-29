package com.yomahub.liteflow.builder.el.operator.ext;

import com.ql.util.express.Operator;

import java.util.HashMap;

public class CreateMapOperator extends Operator {
    @Override
    public Object executeInner(Object[] list) throws Exception {
        return new HashMap<>();
    }
}
