package com.yjfei.antibot.controller;


import com.yjfei.antibot.bean.RuleConditionBean;
import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.common.RiskException;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.dto.AddVariableDTO;
import com.yjfei.antibot.dto.AddVariableDTO.OTFVariableDTO;
import com.yjfei.antibot.dto.AddVariableDTO.NamelistVariableDTO;
import com.yjfei.antibot.dto.AddVariableDTO.RADDVariableDTO;
import com.yjfei.antibot.dto.AddVariableDTO.TumbleVariableDTO;
import com.yjfei.antibot.dto.VariableDTO;
import com.yjfei.antibot.service.RuleService;
import com.yjfei.antibot.service.VariableService;
import com.yjfei.antibot.util.ConvertUtil;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("variable API")
@RestController
@RequestMapping("/variable")
@Slf4j
@SuppressWarnings("all")
public class VariableController {

    @Autowired
    private VariableService variableService;

    @Autowired
    private RuleService ruleService;


    @ApiOperation(value = "添加/修改变量")
    @PostMapping(value = {"/add"})
    public DataResponse<Boolean> add(@Validated @RequestBody AddVariableDTO addVariableDTO) {


        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            VariableBean variableBean = ConvertUtil.convert(addVariableDTO, VariableBean.class);

            if (addVariableDTO.getType() == 0) {
                OTFVariableDTO otfVariableDTO = (OTFVariableDTO) addVariableDTO;
                variableBean.setLogicExpression(otfVariableDTO.getLogicExpression());
            } else if (addVariableDTO.getType() == 1) {
                NamelistVariableDTO namelistVariableDTO = (NamelistVariableDTO) addVariableDTO;

                variableBean.setSourceId(namelistVariableDTO.getNameListId());
                variableBean.setKeyExpression(namelistVariableDTO.getKeyExpression());
            } else if (addVariableDTO.getType() == 2) {
                RADDVariableDTO raddVariableDTO = (RADDVariableDTO) addVariableDTO;
                variableBean.setSourceId(raddVariableDTO.getStreamingVariableId());
                variableBean.setKeyExpression(raddVariableDTO.getKeyExpression());
            } else if (addVariableDTO.getType() == 3) {
                TumbleVariableDTO tumbleVariableDTO = (TumbleVariableDTO) addVariableDTO;

                variableBean.setSourceId(tumbleVariableDTO.getStreamingVariableId());
                variableBean.setKeyExpression(tumbleVariableDTO.getKeyExpression());
                variableBean.setPeriod(tumbleVariableDTO.getPeriod());
                variableBean.setTimeUnit(tumbleVariableDTO.getTimeUnit());
            }else {
                throw new RiskException("type is wrong");
            }

            variableService.addVariable(variableBean);

        } catch (Throwable t) {
            log.error("add variable error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }

    @ApiOperation(value = "查询变量列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<VariableDTO>> list(@ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize) {

        PageListResponse<List<VariableDTO>> pageResponse = new PageListResponse<List<VariableDTO>>();
        try{

            Page<VariableBean> pageResult = variableService.list(page,pageSize);

            List<VariableDTO> variableDTOList = ConvertUtil.convert(pageResult.getResult(),variableBean -> {

                VariableDTO variableDTO = ConvertUtil.convert(variableBean,VariableDTO.class);

                List<RuleConditionBean> ruleConditionBeanList = ruleService.listRuleConditonByVariableId(variableBean.getId());

                List<String> factors = Lists.newArrayList();

                if (!CollectionUtils.isEmpty(ruleConditionBeanList)){
                    for (RuleConditionBean ruleConditionBean:ruleConditionBeanList) {
                        factors.add(ruleConditionBean.getFactor());
                    }
                }

                variableDTO.setFactors(factors);

                if (variableBean.getType() == VariableType.OTF.getCode()){
                    variableDTO.setLogicExpression(variableBean.getLogicExpression());
                }else if (variableBean.getType() == VariableType.NAMELIST.getCode()){
                    variableDTO.setKeyExpression(variableBean.getKeyExpression());
                    variableDTO.setNameListId(variableBean.getSourceId());
                }else if(variableBean.getType() == VariableType.RADD.getCode()){
                    variableDTO.setStreamingVariableId(variableBean.getSourceId());
                    variableDTO.setKeyExpression(variableBean.getKeyExpression());
                }else if(variableBean.getType() == VariableType.ROLLUP.getCode()){
                    variableDTO.setStreamingVariableId(variableBean.getSourceId());
                    variableDTO.setKeyExpression(variableBean.getKeyExpression());
                    variableDTO.setTimeUnit(variableBean.getTimeUnit());
                    variableDTO.setPeriod(variableBean.getPeriod());
                }

                return variableDTO;

            });


            pageResponse.setData(variableDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        }catch (Throwable t){
            log.error("list variable error",t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }


}
