package com.yjfei.antibot.engine.model;

import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.service.DataModelService;

/**
 * 模型字段
 */
@SuppressWarnings("all")
public abstract class ModelField<T> implements Castable<T>,Validable<T> {


    private final ModelFieldBean modelFieldBean;

    private final DataType dataType;

    protected ModelField(ModelFieldBean modelFieldBean){
        this.modelFieldBean = modelFieldBean;
        this.dataType = DataType.of(modelFieldBean.getDataType())
                        .orElseThrow(() -> new IllegalArgumentException("不支持的数据模型字段数据类型编码(" + modelFieldBean.getDataType() + ")"));
    }

    public static ModelField create(DataModelService dataModelService,ModelFieldBean modelFieldBean){
        DataType dataType = DataType.of(modelFieldBean.getDataType()).orElseThrow(() -> new IllegalArgumentException("不支持的数据模型字段数据类型编码(" + modelFieldBean.getDataType() + ")"));

        switch (dataType){
            case LONG:
                return new LongField(modelFieldBean);
            case DOUBLE:
                return new DoubleField(modelFieldBean);
            case STRING:
                return new StringField(modelFieldBean);
            default:
                return new StringField(modelFieldBean);
        }
    }


    public String getCode() {
        return modelFieldBean.getCode();
    }


    public DataType getDataType() {
        return dataType;
    }

    /**
     * 给定的类型是否本类型或本类型的子类型
     *
     * @param aClass 给定的类型
     * @return 给定的类型是否本类型或本类型的子类型
     */
    protected abstract boolean isAssignableFrom(Class aClass);
}
