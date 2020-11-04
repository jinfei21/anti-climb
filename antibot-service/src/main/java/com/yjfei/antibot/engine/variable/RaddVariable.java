package com.yjfei.antibot.engine.variable;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.config.ApplicationContextProvider;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.Expression;
import com.yjfei.antibot.service.RedisService;
import com.yjfei.antibot.service.StreamVariableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

@Slf4j
public class RaddVariable extends Variable<Object> {
    private final Expression keyExpression;
    private final DataType valueDataType;

    private RedisService redisService;

    public RaddVariable(VariableBean variableBean){
        super(variableBean);

        this.keyExpression = new Expression(variableBean.getKeyExpression());

        StreamVariableService streamVariableService = ApplicationContextProvider.getApplicationContext().getBean(StreamVariableService.class);

        StreamVariableBean streamVariableBean = streamVariableService.getVariableById(variableBean.getSourceId());

        this.valueDataType = DataType.of(streamVariableBean.getValueType()).orElseThrow(() -> new IllegalArgumentException("invalid value type: " + streamVariableBean.getValueType()));

        this.redisService = ApplicationContextProvider.getApplicationContext().getBean(RedisService.class);
    }

    @Override
    protected Object evaluate(Context context) {

        Object keyObject = keyExpression.calculate(context);
        if (keyObject == null) {
            return null;
        }
        String key = keyObject.toString();

        Object value = redisService.get(key);

        if (value == null) return null;

        Object result = null;
        switch (valueDataType) {
            case LONG:
                result = NumberUtils.createLong(value.toString());
                break;
            case DOUBLE:
                result = NumberUtils.createDouble(value.toString());
                break;
            default:
                result = value;
                break;
        }
        return result;
    }
}
