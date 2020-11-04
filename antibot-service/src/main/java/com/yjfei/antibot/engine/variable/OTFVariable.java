package com.yjfei.antibot.engine.variable;

import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.Expression;

/**
 * 请求发生时进行的计算，主要是一些key变量
 */
public class OTFVariable extends Variable<Object>{
    private final Expression expression;


    public OTFVariable(VariableBean variableBean){
        super(variableBean);
        this.expression = new Expression(variableBean.getLogicExpression());
    }

    @Override
    protected Object evaluate(Context context) {
        return expression.calculate(context);
    }
}
