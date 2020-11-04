package com.yjfei.antibot.engine.model;

import com.yjfei.antibot.bean.ModelFieldBean;

import javax.validation.ValidationException;

/**
 * 简单类型的字段
 */

public abstract class SimpleField<T> extends ModelField<T> {

    /**
     * 是否必须
     */
    private boolean required;

    /**
     * 默认值
     */
    private T defValue;

    protected SimpleField(ModelFieldBean modelFieldBean){
        super(modelFieldBean);
        this.required = modelFieldBean.getRequired();
        this.defValue = cast(modelFieldBean.getDefValue());
    }

    @Override
    public void validate(T data){
        if (required && data == null){
            throw new ValidationException(getCode() + "is required!");
        }
    }

    public boolean isRequired(){
        return this.required;
    }

    public T getDefValue(){
        return this.defValue;
    }
}
