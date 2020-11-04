package com.yjfei.antibot.stream.workflow;

import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.common.WorkflowType;
import com.yjfei.antibot.stream.engine.Workflow;
import com.yjfei.antibot.stream.engine.WorkflowGroup;
import com.yjfei.antibot.stream.rollup.RollupWorkflow;
import com.yjfei.antibot.stream.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RollupWorkflowGroup extends WorkflowGroup {

    @Autowired
    public RollupWorkflowGroup(WorkflowService workflowService){
        super(workflowService);
    }

    @Override
    protected List<Workflow> initWorkflows() {
        List<WorkflowBean> workflowBeans = workflowService.findWorkflowListByType(WorkflowType.ROLLUP_WORKFLOW);
        return workflowBeans.stream().map(bean -> new RollupWorkflow(super.workflowService,bean)).collect(Collectors.toList());
    }
}
