package com.yjfei.antibot.controller;

import com.yjfei.antibot.bean.RuleBean;
import com.yjfei.antibot.bean.RuleConditionBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.AddRuleDTO;
import com.yjfei.antibot.dto.RuleDTO;
import com.yjfei.antibot.service.RuleService;
import com.yjfei.antibot.util.ConvertUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("rule API")
@RestController
@RequestMapping("/rule")
@Slf4j
@SuppressWarnings("all")
public class RuleController {

    @Autowired
    private RuleService ruleService;


    @ApiOperation(value = "添加/修改规则")
    @PostMapping(value = {"/add"})
    public DataResponse<Boolean> add(@Validated @RequestBody AddRuleDTO addRuleDTO) {
        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            RuleBean ruleBean = ConvertUtil.convert(addRuleDTO, RuleBean.class);

            List<RuleConditionBean> ruleConditionBeanList = ConvertUtil.convert(addRuleDTO.getConditions(), ruleConditionDTO -> {
                RuleConditionBean ruleConditionBean = ConvertUtil.convert(ruleConditionDTO,RuleConditionBean.class);

                return ruleConditionBean;
            });

            ruleService.addRule(ruleBean,ruleConditionBeanList);

        } catch (Throwable t) {
            log.error("add  rule error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }

    @ApiOperation(value = "保存规则顺序")
    @PostMapping(value = {"/savepriority"})
    public DataResponse<Boolean> savepriority(@Validated @RequestBody List<Long> ids){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            ruleService.updateRulePriority(ids);

        } catch (Throwable t) {
            log.error("deactive  rule error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }


    @ApiOperation(value = "禁用规则")
    @PostMapping(value = {"/deactive"})
    public DataResponse<Boolean> deactive(@Validated @RequestBody List<Long> ids) {
        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            ruleService.updateRuleStatus(ids, 0);
        } catch (Throwable t) {
            log.error("deactive  rule error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;

    }

    @ApiOperation(value = "启用规则")
    @PostMapping(value = {"/active"})
    public DataResponse<Boolean> active(@Validated @RequestBody List<Long> ids) {
        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            ruleService.updateRuleStatus(ids, 1);

        } catch (Throwable t) {
            log.error("active  rule error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }

    @ApiOperation(value = "查询规则列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<RuleDTO>> list(
            @ApiParam("规则名称") @RequestParam(value = "strategyId", defaultValue = "") Long strategyId,
            @ApiParam("规则名称") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize) {

        PageListResponse<List<RuleDTO>> pageResponse = new PageListResponse<List<RuleDTO>>();

        try {
            if (StringUtils.isEmpty(name)) name =null;
            Page<RuleBean> pageResult = ruleService.list(strategyId,name, page, pageSize);

            List<RuleDTO> ruleDTOList = ConvertUtil.convert(pageResult.getResult(), ruleBean -> {
                RuleDTO ruleDTO = ConvertUtil.convert(ruleBean, RuleDTO.class);

                String logicString = ruleService.getLogicStringByRule(ruleBean);
                ruleDTO.setLogicString(logicString);
                return ruleDTO;
            });

            pageResponse.setData(ruleDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        } catch (Throwable t) {
            log.error("list rule error", t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;

    }

}
