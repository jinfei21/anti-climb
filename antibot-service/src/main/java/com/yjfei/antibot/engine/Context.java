package com.yjfei.antibot.engine;


import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.engine.variable.Variable;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Context {

    private final String strategyCode;

    @Getter
    private final DataSet request;

    @Getter
    private final long timestamp;

    private final Map<Long,Object> variables;

    private final Map<Expression, Object> expressions;

    public Context(String strategyCode,DataSet dataSet){
        this(strategyCode,dataSet,System.currentTimeMillis());
    }

    public Context(String strategyCode,DataSet dataSet,long timestamp){
        this.strategyCode = strategyCode;
        this.timestamp = timestamp;
        this.request = dataSet;
        this.variables = new HashMap<>();
        this.expressions = new HashMap<>();
    }

    public boolean containVariable(long variableId){
        return variables.containsKey(variableId);
    }

    public Object getVariable(long variableId){
        return variables.get(variableId);
    }


    public boolean containsExpression(Expression expression) {
        return expressions.containsKey(expression);
    }

    public Object getExpression(Expression expression) {
        return expressions.get(expression);
    }

    public void putExpression(Expression expression, Object value) {
        expressions.put(expression, value);
    }

    public void log(Variable variable, Object result) {
        this.variables.put(variable.getId(), result);
    }


}
