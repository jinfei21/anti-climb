package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.common.WorkflowType;
import com.yjfei.antibot.stream.dao.WorkflowMapper;
import com.yjfei.antibot.stream.engine.DataSource;
import com.yjfei.antibot.stream.engine.Variable;
import com.yjfei.antibot.stream.engine.VariableValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkflowService {

    private final WorkflowMapper workflowMapper;
    private final DataSourceService dataSourceService;
    private final ComplexVariableService variableService;

    @Autowired
    public WorkflowService(WorkflowMapper workflowMapper,DataSourceService dataSourceService,ComplexVariableService variableService){
        this.workflowMapper = workflowMapper;
        this.dataSourceService = dataSourceService;
        this.variableService = variableService;
    }


    /**
     * 根据类型查找所有 workflow
     *
     * @return  workflow
     */
    public List<WorkflowBean> findWorkflowListByType(WorkflowType workflowType) {
        return workflowMapper.selectByType(workflowType.getCode());
    }

    public DataSource getDataSource(long dataSourceId){
        return dataSourceService.getDataSource(dataSourceId);
    }

    public <T extends Variable> List<T> getVariableList(long workflowId,VariableType variableType,Class<T> variableTypeClazz){

        List<StreamVariableBean> streamVariableBeanList = variableService.getVariableListByWorkflowId(workflowId,variableType);

        return streamVariableBeanList.stream()
                .map(variableBean -> {
                    try {
                        Constructor<T> constructor = variableTypeClazz.getConstructor(StreamVariableBean.class);
                        constructor.setAccessible(true);
                        return constructor.newInstance(variableBean);
                    } catch (Exception e) {
                        log.error("wrong implementation of variable type {}", variableTypeClazz.getName(), e);
                        throw new IllegalArgumentException("错误的变量实现" + variableTypeClazz.getName());
                    }
                })
                .collect(Collectors.toList());
    }

    public void saveVariableValues(VariableType variableType, long timestamp, List<VariableValue> variableValueList){
        variableService.save(variableType,timestamp,variableValueList);
    }

}
