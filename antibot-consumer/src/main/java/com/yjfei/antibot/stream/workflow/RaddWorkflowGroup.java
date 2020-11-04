package com.yjfei.antibot.stream.workflow;

import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.common.WorkflowType;
import com.yjfei.antibot.stream.engine.Workflow;
import com.yjfei.antibot.stream.engine.WorkflowGroup;
import com.yjfei.antibot.stream.radd.RaddWorkflow;
import com.yjfei.antibot.stream.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RaddWorkflowGroup extends WorkflowGroup {


    @Autowired
    public RaddWorkflowGroup(WorkflowService workflowService){
        super(workflowService);
    }
    @Override
    protected List<Workflow> initWorkflows() {
        List<WorkflowBean> workflowBeanList = workflowService.findWorkflowListByType(WorkflowType.RADD_WORKFLOW);
        return workflowBeanList.stream()
                .map(bean -> new RaddWorkflow(workflowService,bean))
                .collect(Collectors.toList());
    }
}
