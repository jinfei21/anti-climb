package com.yjfei.antibot.engine.model;

import com.yjfei.antibot.bean.ModelFieldBean;

public class StringField extends SimpleField<String> {

    public StringField(ModelFieldBean modelFieldBean){
        super(modelFieldBean);
    }

    @Override
    protected boolean isAssignableFrom(Class aClass) {
        return Character.class.isAssignableFrom(aClass)||String.class.isAssignableFrom(aClass);
    }

    @Override
    public String cast(Object original) {

        if (original == null){
            return getDefValue();
        }else if(isAssignableFrom(original.getClass())){
            return original.toString();
        }
        throw new ClassCastException(original.getClass().getName() + " can't be cast to String, value is " + original.toString() + ", field code is " + getCode());
    }
}
