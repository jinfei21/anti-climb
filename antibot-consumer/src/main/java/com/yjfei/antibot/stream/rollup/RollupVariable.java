package com.yjfei.antibot.stream.rollup;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.stream.engine.Context;
import com.yjfei.antibot.stream.engine.Expression;
import com.yjfei.antibot.stream.engine.Variable;
import com.yjfei.antibot.stream.engine.VariableValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RollupVariable extends Variable {

    /**
     * key表达式
     */
    private final Expression keyExp;

    /**
     * 过滤条件
     */
    private final Expression filterExp;

    /**
     * value表达式
     */
    private final Expression valueExp;

    public RollupVariable(StreamVariableBean streamVariableBean) {
        super(streamVariableBean);
        this.keyExp = new Expression(streamVariableBean.getKeyPattern());
        this.filterExp = new Expression(streamVariableBean.getFilterPattern());
        this.valueExp = new Expression(streamVariableBean.getAggPattern());
    }

    @Override
    protected VariableValue evaluate(Context context) {
        Object keyObject = keyExp.calculate(context);

        if (keyObject == null){
            log.warn("key is missing, variable is {}, context is {}", this.getBean().getId(), context);
            return VariableValue.NULL;
        }

        String key = keyObject.toString();
        Boolean condition = (Boolean) filterExp.calculate(context);

        Object value = Boolean.TRUE.equals(condition)?valueExp.calculate(context):null;

        return new VariableValue(bean,key,value,context.getTimestamp());
    }
}
