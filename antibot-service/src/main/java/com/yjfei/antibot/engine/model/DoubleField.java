package com.yjfei.antibot.engine.model;

import com.yjfei.antibot.bean.ModelFieldBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Float/Double类型的字段
 */
@Slf4j
public class DoubleField extends SimpleField<Double> {

    public DoubleField(ModelFieldBean modelFieldBean){
        super(modelFieldBean);
    }

    @Override
    protected boolean isAssignableFrom(Class aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public Double cast(Object original) {

        if (original == null){
            return getDefValue();
        }else if (String.class.isAssignableFrom(original.getClass())){
            try {
                return NumberUtils.createNumber((String) original).doubleValue();
            } catch (NumberFormatException ignore) {
                log.warn("failed to format value to double, original value is {}", original);
            }
        }else if (isAssignableFrom(original.getClass())){
            return ((Number)original).doubleValue();
        }
        throw new ClassCastException(original.getClass().getName() + " can't be cast to Double, value is " + original.toString() + ", field code is " + getCode());    }
}
