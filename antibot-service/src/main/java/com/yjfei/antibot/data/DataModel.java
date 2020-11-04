package com.yjfei.antibot.data;

import com.yjfei.antibot.bean.ModelBean;
import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.engine.model.ModelField;
import com.yjfei.antibot.engine.model.Validable;
import com.yjfei.antibot.service.DataModelService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DataModel implements Validable<DataSet> {
    private static final String GET_CLASS_METHOD_NAME = "getClass";
    private static final String GET_METHOD_PREFIX = "get";
    private static final String IS_METHOD_PREFIX = "is";

    private static final Map<String, Method> METHOD_MAP = new ConcurrentHashMap<>();

    private final ModelBean modelBean;

    /**
     * 字段列表
     */
    private final Map<String, ModelField> fields;

    public DataModel(DataModelService dataModelService, ModelBean modelBean, List<ModelFieldBean> modelFieldBeanList){
        this.modelBean = modelBean;
        Map<String, ModelField> modelFieldMap = modelFieldBeanList.stream().map(modelFieldBean -> ModelField.create(dataModelService,modelFieldBean))
                .collect(Collectors.toMap(ModelField::getCode, Function.identity()));
        this.fields = Collections.unmodifiableMap(modelFieldMap);
    }

    public DataSet cast(Map<String,Object> source){
        if (source == null){
            return DataSet.empty();
        }

        DataSet dataSet = new DataSet(this);

        source.forEach((key,value)->{
            if (fields.containsKey(key)){
                ModelField modelField = fields.get(key);
                Object result = modelField.cast(value);
                dataSet.add(modelField,result);
            }
        });

        return dataSet;
    }


    /**
     * 转换数据
     *
     * @param source 待转换的数据
     * @return 转换后的数据
     */
    public DataSet cast(Object source) {
        if (source == null) {
            return DataSet.empty();
        }

        DataSet dataSet = new DataSet(this);
        Method[] methods = source.getClass().getMethods();

        Arrays.stream(methods).forEach(method -> {
            String fieldName = getFieldName(method);
            if (fieldName != null && fields.containsKey(fieldName)) {
                Object value = getValue(method, source);
                ModelField modelField = fields.get(fieldName);
                Object result = modelField.cast(value);
                dataSet.add(modelField, result);
            }
        });
        return dataSet;
    }


    private String getFieldName(Method method) {
        if (method.getParameterCount() > 0) {
            return null;
        }
        if (!GET_CLASS_METHOD_NAME.equals(method.getName())
                && method.getName().startsWith(GET_METHOD_PREFIX)) {
            return Character.toLowerCase(method.getName().charAt(3))
                    + method.getName().substring(4);
        } else if (method.getName().startsWith(IS_METHOD_PREFIX)) {
            return Character.toLowerCase(method.getName().charAt(2))
                    + method.getName().substring(3);
        }
        return null;
    }

    private Object getValue(final Method method, Object obj) {
        String methodFullName = method.getDeclaringClass().getName() + "#" + method.getName();
        Method accessMethod = METHOD_MAP.computeIfAbsent(methodFullName,
                s -> {
                    method.setAccessible(true);
                    return method;
                });
        try {
            return accessMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("log when get value from class {} method {}", method.getDeclaringClass().getName(), method.getName(), e);
            return null;
        }
    }


    @Override
    public void validate(DataSet data) {

        if (data == null){
            return;
        }

        fields.forEach((fieldCode,field)->{
            Object fieldVal = data.get(fieldCode);
            field.validate(fieldVal);
        });
    }

    /**
     * 包含的字段数量
     *
     * @return 字段数量
     */
    public int getFieldCount() {
        return fields.size();
    }

    public boolean hasField(ModelField modelField) {
        return fields.containsKey(modelField.getCode());
    }

    public long getId() {
        return modelBean.getId();
    }

    public String getName() {
        return modelBean.getName();
    }
}
