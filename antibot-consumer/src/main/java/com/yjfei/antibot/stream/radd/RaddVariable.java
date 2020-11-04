package com.yjfei.antibot.stream.radd;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.stream.engine.Context;
import com.yjfei.antibot.stream.engine.Expression;
import com.yjfei.antibot.stream.engine.Variable;
import com.yjfei.antibot.stream.engine.VariableValue;

public class RaddVariable extends Variable {

    private String key;

    private String value;

    private final Expression keyExp;

    private final Expression valueExp;


    public RaddVariable(StreamVariableBean streamVariableBean) {
        super(streamVariableBean);
        this.key = streamVariableBean.getKeyPattern();
        this.value = streamVariableBean.getAggPattern();
        this.keyExp = new Expression(streamVariableBean.getKeyPattern());

        this.valueExp = new Expression(streamVariableBean.getAggPattern());
    }

    @Override
    protected VariableValue evaluate(Context context) {

//        Map<String,Object> data = (Map<String, Object>) context.get("data");
        //String variableKey = context.get(this.key).toString();
        //Object variableValue = context.get(this.value);
        Object keyObject = keyExp.calculate(context);
        Object valueObject = valueExp.calculate(context);

        return new VariableValue(bean,String.valueOf(keyObject),valueObject,context.getTimestamp());
    }
}
