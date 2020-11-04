package com.yjfei.antibot.engine.model;

import com.yjfei.antibot.data.DataModel;

import java.util.HashMap;
import java.util.Map;

public class DataSet extends HashMap<String,Object> {
    private static final DataSet EMPTY = new DataSet();

    private final DataModel dataModel;

    private DataSet(){
        this.dataModel = null;
    }

    public DataSet(DataModel dataModel){
        super(dataModel.getFieldCount());
        this.dataModel = dataModel;
    }


    /**
     * 空的数据集
     *
     * @return 空的数据集
     */
    public static DataSet empty() {
        return EMPTY;
    }

    /**
     * 添加一个字段值
     *
     * @param modelField 字段信息
     * @param result     待添加的字段值
     */
    public void add(ModelField modelField, Object result) {

        if (isBelonged(modelField)){
            put(modelField.getCode(),result);
        }
    }

    private boolean isBelonged(ModelField modelField) {
        return dataModel.hasField(modelField);
    }

    public void validate(){
        if (dataModel == null){
            return;
        }
        dataModel.validate(this);
    }

    public Map<String,Object> getData(){
        return this;
    }

    public DataModel getDataModel(){
        return this.dataModel;
    }

    public String getDataModelName(){
        return this.dataModel.getName();
    }

    /**
     * 根据字段编码获取字段值
     *
     * @param fieldCode 字段编码
     * @param <T>       字段值类型
     * @return 字段值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String fieldCode) {
        return (T) super.get(fieldCode);
    }
}
