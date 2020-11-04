package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.dao.StreamVariableMapper;
import com.yjfei.antibot.dao.WorkflowMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkflowService {

    @Autowired
    private WorkflowMapper workflowMapper;

    @Autowired
    private StreamVariableMapper streamVariableMapper;

    public void addWorkflow(WorkflowBean workflowBean, List<StreamVariableBean> variableBeanList){
        if (workflowBean.getId() == null){
            workflowBean.setCreateBy("admin");
            workflowBean.setUpdateBy(workflowBean.getCreateBy());
            workflowMapper.insert(workflowBean);

            for (StreamVariableBean streamVariableBean:variableBeanList){
                streamVariableBean.setWorkflowId(workflowBean.getId());
                streamVariableMapper.insert(streamVariableBean);
            }
        }else{
            workflowBean.setUpdateBy("admin");
            workflowMapper.updateByPrimaryKeySelective(workflowBean);

            List<StreamVariableBean> oldVariableBeanList = streamVariableMapper.selectByWorkflowId(workflowBean.getId());

            Map<Long,StreamVariableBean> needDeleteVariableMap = oldVariableBeanList.stream().collect(Collectors.toMap(StreamVariableBean::getId,variableBean->variableBean));

            variableBeanList.forEach(variableBean->{
                variableBean.setWorkflowId(workflowBean.getId());
                variableBean.setUpdateBy("admin");
                if (variableBean.getId() == null){
                    variableBean.setCreateBy(variableBean.getUpdateBy());
                    streamVariableMapper.insert(variableBean);
                }else {
                    needDeleteVariableMap.remove(variableBean.getId());
                    streamVariableMapper.updateByPrimaryKeySelective(variableBean);
                }
            });

            if (!CollectionUtils.isEmpty(needDeleteVariableMap)){
                needDeleteVariableMap.forEach((id,variableBean)->{
                    streamVariableMapper.deleteByPrimaryKey(id);
                });
            }
        }
    }

    @Transactional
    public void deleteWorkflowById(Long id){
        workflowMapper.deleteByPrimaryKey(id);
        streamVariableMapper.deleteByWorkflowId(id);
    }

    public WorkflowBean getWorkflowById(Long id){
        return workflowMapper.selectByPrimaryKey(id);
    }

    public List<StreamVariableBean> findVariableByWorkflowId(Long workflowId){
        return  streamVariableMapper.selectByWorkflowId(workflowId);
    }

    public Page<WorkflowBean> list(Integer page, Integer pageSize){

        Page<WorkflowBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> workflowMapper.selectAll());

        return pageResult;
    }
}
