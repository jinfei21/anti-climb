package com.yjfei.antibot.stream.radd;

import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.engine.Context;
import com.yjfei.antibot.stream.engine.DataSource;
import com.yjfei.antibot.stream.engine.VariableValue;
import com.yjfei.antibot.stream.engine.Workflow;
import com.yjfei.antibot.stream.service.WorkflowService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class RaddWorkflow extends Workflow {

    @Getter
    private final long id;

    @Getter
    private final String name;

    private final DataSource dataSource;

    private final WorkflowService workflowService;

    private final List<RaddVariable> variableList;

    /**
     * 运行状态
     */
    private final AtomicBoolean state = new AtomicBoolean(false);

    public RaddWorkflow(WorkflowService workflowService, WorkflowBean workflowBean){
        this.id = workflowBean.getId();
        this.name = workflowBean.getName();
        this.dataSource = workflowService.getDataSource(workflowBean.getDataSourceId());
        this.variableList = workflowService.getVariableList(workflowBean.getId(), VariableType.RADD,RaddVariable.class);
        this.workflowService = workflowService;
    }


    @Override
    protected void init() {
        log.info("initialize radd workflow {}-{}",this.id,this.name);

        dataSource.connectWith(message -> {
            StopWatch stopWatch = StopWatch.createStarted();

            Context context = new Context(message);
            List<VariableValue> variableValueList = this.variableList.stream()
                    .map(variable -> variable.evaluate(context))
                    .filter(VariableValue::isNotEmpty)
                    .collect(Collectors.toList());

            workflowService.saveVariableValues(VariableType.RADD,message.getTimestamp(),variableValueList);
            stopWatch.stop();
            log.info("execution time of radd work flow {}-{} is {}", this.id, this.name, stopWatch.getTime());
            return true;
        });
    }

    @Override
    public void start() {
        log.info("starting radd work flow {}-{}", this.id, this.name);
        dataSource.start();
        state.set(true);
        log.info("radd work flow {}-{} has been started successfully", this.id, this.name);
    }

    @Override
    public void stop() {
        log.info("stopping radd work flow {}-{}", this.id, this.name);
        dataSource.stop();
        log.info("radd work flow {}-{} has been stopped successfully", this.id, this.name);
    }

    @Override
    public boolean isRunning() {
        return state.get();
    }
}
