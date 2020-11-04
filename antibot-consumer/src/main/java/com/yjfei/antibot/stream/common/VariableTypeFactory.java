package com.yjfei.antibot.stream.common;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.engine.Variable;
import com.yjfei.antibot.stream.radd.RaddVariable;
import com.yjfei.antibot.stream.rollup.RollupVariable;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings("all")
public class VariableTypeFactory {
    private static VariableTypeFactory instance;

    private Map<VariableType,Class<? extends Variable>> variableTypeMap = new HashMap<>();

    private VariableTypeFactory(){

        this.variableTypeMap.put(VariableType.ROLLUP, RollupVariable.class);
        this.variableTypeMap.put(VariableType.RADD, RaddVariable.class);
    }

    public static VariableTypeFactory getInstance(){
        if (instance == null){
            synchronized (VariableTypeFactory.class){
                if (instance == null){
                    instance = new VariableTypeFactory();
                }
            }
        }
        return instance;
    }

    public Variable getVariable(VariableType variableType, StreamVariableBean streamVariableBean){
        try {
            Class<? extends Variable> variableClazz = this.variableTypeMap.get(variableType);
            Constructor constructor = variableClazz.getConstructor(StreamVariableBean.class);
            constructor.setAccessible(true);
            return (Variable) constructor.newInstance(streamVariableBean);
        } catch (Exception e) {
            log.error("wrong implementation of variable type {}", variableType.getText(), e);
            throw new IllegalArgumentException("variable type is wrong:" + variableType.getText());
        }
    }

}
