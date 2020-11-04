package com.yjfei.antibot.stream.workflow;

import com.yjfei.antibot.stream.engine.Workflow;
import com.yjfei.antibot.stream.engine.WorkflowGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 在应用启动后自动启动所有的workflow
 */
@Slf4j
@Component
public class WorkflowRunner implements ApplicationRunner {
    private final Map<String, WorkflowGroup> workflowGroupMap;
    private final Map<String, Workflow> workflowMap;

    @Autowired
    public WorkflowRunner(Map<String, WorkflowGroup> workflowGroupMap, Map<String, Workflow> workflowMap) {
        this.workflowGroupMap = workflowGroupMap;
        this.workflowMap = workflowMap;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Workflow> workflowList = workflowGroupMap.values().stream()
                .map(WorkflowGroup::getWorkflows)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Workflow> allWorkflowList = new ArrayList<>(workflowList.size() + workflowMap.size());
        allWorkflowList.addAll(workflowList);
        allWorkflowList.addAll(workflowMap.values());

        log.info("begin to start all {} spring registered work flows...", allWorkflowList.size());

        allWorkflowList.forEach(Workflow::start);

        log.info("all {} spring registered work flows have been started", allWorkflowList.size());
    }

}
