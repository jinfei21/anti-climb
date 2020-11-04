package com.yjfei.antibot.controller;


import com.yjfei.antibot.bean.StrategyBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.AddStrategyDTO;
import com.yjfei.antibot.dto.StrategyDTO;
import com.yjfei.antibot.dto.StrategyRuleDTO;
import com.yjfei.antibot.service.RuleService;
import com.yjfei.antibot.service.StrategyService;
import com.yjfei.antibot.util.ConvertUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("strategy API")
@RestController
@RequestMapping("/strategy")
@Slf4j
@SuppressWarnings("all")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private RuleService ruleService;


    @ApiOperation(value = "添加/修改策略")
    @PostMapping(value = {"/add"})
    public DataResponse<Boolean> add(@Validated @RequestBody AddStrategyDTO addStrategyDTO){

        DataResponse<Boolean> response = new DataResponse<>(true);
        try {
            StrategyBean strategyBean = ConvertUtil.convert(addStrategyDTO,StrategyBean.class);

            strategyService.addStrategy(strategyBean);

        } catch (Throwable t) {
            log.error("add  strategy error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }

    @ApiOperation(value = "查询策略")
    @GetMapping(value = {"/getStrategyById/{id}"})
    public DataResponse<StrategyDTO> getById(@PathVariable("id")Long id ) {
        DataResponse<StrategyDTO> response = new DataResponse<StrategyDTO>();

        try{

            StrategyBean strategyBean = strategyService.getStrategyById(id);
            StrategyDTO strategyDTO = ConvertUtil.convert(strategyBean,StrategyDTO.class);
            response.setData(strategyDTO);

        }catch (Throwable t){
            log.error("getStrategyById {} error",id,t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }

    @ApiOperation(value = "获取策略规则列表")
    @GetMapping(value = {"/getRulesById/{id}"})
    public DataResponse<List<StrategyRuleDTO>> getRulesById(@PathVariable("id")Long id ) {
        DataResponse<List<StrategyRuleDTO>> response = new DataResponse<List<StrategyRuleDTO>>();
        try{

            List<StrategyRuleDTO> strategyRuleDTOList = ruleService.getAllRuleByStrategyId(id);

            response.setData(strategyRuleDTOList);
        }catch (Throwable t){
            log.error("getRulesById {} error",id,t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }


    @ApiOperation(value = "删除策略")
    @DeleteMapping(value = {"/delete/{id}"})
    public DataResponse<Boolean> delete(@PathVariable("id")Long id ) {
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{

            strategyService.deleteStrategyById(id);
        }catch (Throwable t){
            log.error("delete strategy error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;

    }


    @ApiOperation(value = "查询变量列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<StrategyDTO>> list(@ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize) {


        PageListResponse<List<StrategyDTO>> pageResponse = new PageListResponse<List<StrategyDTO>>();

        try{

            Page<StrategyBean> pageResult = strategyService.list(page,pageSize);

            List<StrategyDTO> strategyDTOList = ConvertUtil.convert(pageResult.getResult(),strategyBean -> {
                StrategyDTO strategyDTO = ConvertUtil.convert(strategyBean,StrategyDTO.class);

                return strategyDTO;
            });
            pageResponse.setData(strategyDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        }catch (Throwable t){
            log.error("list strategy error",t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;

    }


}
