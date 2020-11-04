package com.yjfei.antibot.engine.variable;


import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.rule.RuleVariable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

@Slf4j
public abstract class Variable<R> implements RuleVariable<R> {

    @Getter
    private final long id;

    @Getter
    private final String name;

    @Getter
    private VariableType variableType;

    public Variable(VariableBean variableBean) {
        this.id = variableBean.getId();
        this.name = variableBean.getName();
        this.variableType = VariableType.toType(variableBean.getType());
    }

    @Override
    public final R calculate(Context context) {
        StopWatch stopWatch = StopWatch.createStarted();
        R result;
        if (context.containVariable(this.id)) {
            return (R) context.getVariable(this.id);
        }

        try {
            result = evaluate(context);
        } catch (Exception e) {
            log.error("exception when calculating variable, variable id is {} , context is {}",
                    this.id, context, e);
            result = null;
        }

        context.log(this,result);
        long executionTime = stopWatch.getTime();
        log.info("execution of variable {}-{} is {}", id, name, executionTime);
        return result;
    }

    /**
     * 计算变量
     *
     * @param context 计算上下文
     * @return 计算结果
     */
    protected abstract R evaluate(Context context);


    @Override
    public String toString() {
        return "variable(" + "id=" + id + ", name=" + name + ')';
    }

}
