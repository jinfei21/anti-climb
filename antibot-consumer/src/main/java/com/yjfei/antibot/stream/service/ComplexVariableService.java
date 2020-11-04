package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.dao.VariableMapper;
import com.yjfei.antibot.stream.engine.VariableValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ComplexVariableService {


    private final VariableMapper variableMapper;

    private final Map<VariableType,VariableService> variableServiceMap;

    @Autowired
    public ComplexVariableService(VariableMapper variableMapper, List<VariableService> variableServiceList){
        this.variableMapper = variableMapper;
        this.variableServiceMap = variableServiceList.stream()
                .filter(VariableService::directSupport)
                .collect(Collectors.toMap(VariableService::supportType, Function.identity()));
    }

    public void save(VariableType variableType, long timestamp, List<VariableValue> variableValueList){
        variableServiceMap.get(variableType).save(timestamp,variableValueList);
    }

    public List<StreamVariableBean> getVariableListByWorkflowId(long workflowId, VariableType variableType){
        return variableMapper.selectByWorkflowIdAndType(workflowId,variableType.getCode());
    }

}
