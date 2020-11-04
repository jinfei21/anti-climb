package com.yjfei.antibot.stream.rollup;

import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.data.Message;
import com.yjfei.antibot.stream.engine.*;
import com.yjfei.antibot.stream.service.WorkflowService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class RollupWorkflow extends Workflow {

    @Getter
    private final long id;

    @Getter
    private final String name;

    private final DataSource dataSource;

    private final Expression pkPattern;

    private final Expression etlPattern;

    private final Expression tsPattern;

    private final List<RollupVariable> variableList;

    private final WorkflowService workflowService;

    /**
     * 运行状态
     */
    private final AtomicBoolean state = new AtomicBoolean(false);

    public RollupWorkflow(WorkflowService workflowService, WorkflowBean workflowBean){
        this.id = workflowBean.getId();
        this.name = workflowBean.getName();
        this.dataSource = workflowService.getDataSource(workflowBean.getDataSourceId());
        this.pkPattern = new Expression(workflowBean.getPkPattern());

        if (workflowBean.getEtlPattern() != null){
            this.etlPattern = new Expression(workflowBean.getEtlPattern());
        }else {
            this.etlPattern = null;
        }
        this.tsPattern = new Expression(workflowBean.getTsPattern());

        this.workflowService = workflowService;

        this.variableList = workflowService.getVariableList(workflowBean.getId(), VariableType.ROLLUP,RollupVariable.class);

    }

    @Override
    protected void init() {

        log.info("initialize rollup workflow {}-{}",this.id,this.name);

        dataSource.connectWith(message -> {
            StopWatch stopWatch = StopWatch.createStarted();

            try{

                Context context = new Context(message);

                context.put("message",message);
                message.setId(pkPattern.calculate(context).toString());
                Object timestamp = tsPattern.calculate(context);
                if (timestamp instanceof Double){
                    message.setTimestamp(((Double)timestamp).longValue());
                }
                //message.setTimestamp(timestamp != null?Long.parseLong(timestamp.toString()):System.currentTimeMillis());

                List<Message> messageList;

                if (etlPattern != null){
                    messageList = (List<Message>) etlPattern.calculate(context);
                    messageList.forEach(msg ->{
                        msg.setId(message.getId());
                        msg.setTimestamp(message.getTimestamp());
                    });
                }else {
                    messageList = Collections.singletonList(message);
                }

                List<VariableValue> variableValueList = messageList.stream()
                        .map(this::calculateVariableValues)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                workflowService.saveVariableValues(VariableType.ROLLUP,message.getTimestamp(),variableValueList);

            }catch (Exception e){
                log.error("exception when handling message: {}", message, e);
                return false;
            }
            stopWatch.stop();
            log.info("execution time of rollup work flow {}-{} is {}", this.id, this.name, stopWatch.getTime());
            return true;
        });
    }

    private List<VariableValue> calculateVariableValues(Message message){
        Context context = new Context(message);

        return variableList.stream()
                .map(variable -> variable.calculate(context))
                .filter(VariableValue::isNotEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public void start() {
        log.info("starting rollup work flow {}-{}", this.id, this.name);
        dataSource.start();
        state.set(true);
        log.info("rollup work flow {}-{} has been started successfully", this.id, this.name);
    }

    @Override
    public void stop() {
        log.info("stopping rollup work flow {}-{}", this.id, this.name);
        dataSource.stop();
        log.info("rollup work flow {}-{} has been stopped successfully", this.id, this.name);
    }

    @Override
    public boolean isRunning() {
        return state.get();
    }
}
