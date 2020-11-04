package com.yjfei.antibot.engine.model;

import com.yjfei.antibot.bean.ModelFieldBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Byte/Short/Integer/Long类型的字段
 */
@Slf4j
public class LongField extends SimpleField<Long>{

    public LongField(ModelFieldBean modelFieldBean){
        super(modelFieldBean);
    }

    @Override
    protected boolean isAssignableFrom(Class aClass) {
        return Byte.class.isAssignableFrom(aClass)
                || Short.class.isAssignableFrom(aClass)
                || Integer.class.isAssignableFrom(aClass)
                || Long.class.isAssignableFrom(aClass);
    }

    @Override
    public Long cast(Object original) {
        if (original == null){
            return getDefValue();
        }else if (String.class.isAssignableFrom(original.getClass())){
            try {
                return NumberUtils.createNumber((String) original).longValue();
            } catch (NumberFormatException ignore) {
                log.warn("failed to format value to long, original value is {}", original);
            }
        }else if (isAssignableFrom(original.getClass())){
            return ((Number) original).longValue();
        }
        throw new ClassCastException(original.getClass().getName() + " can't be cast to Long, value is " + original.toString() + ", field code is " + getCode());
    }
}
