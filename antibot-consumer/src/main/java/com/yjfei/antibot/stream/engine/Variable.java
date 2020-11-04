package com.yjfei.antibot.stream.engine;

import com.yjfei.antibot.bean.StreamVariableBean;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public abstract class Variable implements Calculator<VariableValue> {
    /**
     * 变量ID
     */
    protected final StreamVariableBean bean;

    protected Variable(StreamVariableBean bean) {
        this.bean = bean;
    }


    @Override
    public VariableValue calculate(Context context) {

        if (context.containsVariableValue(bean.getId())){
            return context.getVariableValue(bean.getId());
        }

        try{
            VariableValue variableValue = evaluate(context);
            log.debug("context is {}, variable is {}, value is {}", context, bean.getId(), variableValue);

            context.putVariableValue(bean.getId(), variableValue);
            return variableValue;
        }catch (Exception e){
            log.error("exception when calculating variable value, variable is {}, context is {}",
                    bean, context, e);
            context.putVariableValue(bean.getId(), VariableValue.NULL);
            return VariableValue.NULL;
        }
    }


    /**
     * 计算变量值
     *
     * @param context 计算上下文
     * @return 变量值
     */
    protected abstract VariableValue evaluate(Context context);
}
