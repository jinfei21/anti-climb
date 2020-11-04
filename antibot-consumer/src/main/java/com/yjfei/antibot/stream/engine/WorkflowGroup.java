package com.yjfei.antibot.stream.engine;

import com.yjfei.antibot.stream.service.WorkflowService;
import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.List;

/**
 * 工作流集合
 */
public abstract class WorkflowGroup implements InitializingBean, DisposableBean {

    @Getter
    protected final List<Workflow> workflows;

    protected final WorkflowService workflowService;

    public WorkflowGroup(WorkflowService workflowService){
        this.workflowService = workflowService;
        this.workflows = Collections.unmodifiableList(this.initWorkflows());
    }

    @Override
    public void afterPropertiesSet() {
        workflows.forEach(Workflow::init);
    }

    @Override
    public void destroy() {
        workflows.forEach(Workflow::stop);
    }

    /**
     * 获取组内所有工作流
     *
     * @return 组内所有工作流
     */
    protected abstract List<Workflow> initWorkflows();
}
