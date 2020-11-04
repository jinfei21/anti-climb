package com.yjfei.antibot.controller;


import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.data.DataModel;
import com.yjfei.antibot.dto.TestRuleDTO;
import com.yjfei.antibot.dto.TestVariableDTO;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.engine.rule.RuleResult;
import com.yjfei.antibot.service.RiskDetectService;
import com.yjfei.antibot.service.RuleTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@Api("rule test API")
@RestController
@RequestMapping("/test")
@Slf4j
@SuppressWarnings("all")
public class RuleTestController {


    @Autowired
    private RuleTestService ruleTestService;

    @Autowired
    private RiskDetectService riskDetectService;


    @ApiOperation(value = "测试规则")
    @PostMapping(value = {"/rule"})
    public DataResponse<RuleResult> testRule(@RequestBody TestRuleDTO testRuleDTO){
        DataResponse<RuleResult> response = new DataResponse<>();
        try{

            DataModel dataModel = riskDetectService.getDataModelByCode(testRuleDTO.getStrategyCode());

            if (dataModel == null){
                response.setCode(500);
                response.setResultMessage("strategyCode is not existed.");
                log.warn("strategyCode:"+testRuleDTO.getStrategyCode()+" is not existed.");
                return response;
            }

            DataSet dataSet = dataModel.cast(testRuleDTO.getData());

            dataSet.validate();

            Context context = new Context(testRuleDTO.getStrategyCode(), dataSet);

            RuleResult ruleResult = ruleTestService.testRule(testRuleDTO.getRuleId(),context);

            response.setData(ruleResult);
        }catch (Throwable t){
            log.error("test rule {} error:{}.",testRuleDTO.getRuleId(),t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }

    @ApiOperation(value = "测试变量")
    @PostMapping(value = {"/variable"})
    public DataResponse<Object> testVariable(@RequestBody TestVariableDTO testVariableDTO){
        DataResponse<Object> response = new DataResponse<>();
        try{

            DataModel dataModel = riskDetectService.getDataModelByCode(testVariableDTO.getStrategyCode());

            if (dataModel == null){
                response.setCode(500);
                response.setResultMessage("strategyCode is not existed.");
                log.warn("strategyCode:"+testVariableDTO.getStrategyCode()+" is not existed.");
                return response;
            }

            DataSet dataSet = dataModel.cast(testVariableDTO.getData());

            dataSet.validate();

            Context context = new Context(testVariableDTO.getStrategyCode(), dataSet);

            if (testVariableDTO.getTimestamp() != null){
                context = new Context(testVariableDTO.getStrategyCode(), dataSet,testVariableDTO.getTimestamp());
            }

            Object value = ruleTestService.testVariable(testVariableDTO.getVariableId(),context);

            response.setData(value);
        }catch (Throwable t){
            log.error("test variable {} error:{}.",testVariableDTO.getVariableId(),t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }

}
