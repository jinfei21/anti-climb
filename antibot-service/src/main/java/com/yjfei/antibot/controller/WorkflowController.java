package com.yjfei.antibot.controller;


import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.bean.WorkflowBean;
import com.yjfei.antibot.dto.AddWorkflowDTO;
import com.yjfei.antibot.dto.StreamVariableDTO;
import com.yjfei.antibot.dto.WorkflowDTO;
import com.yjfei.antibot.dto.WorkflowDetailInfoDTO;
import com.yjfei.antibot.service.WorkflowService;
import com.yjfei.antibot.util.ConvertUtil;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.yjfei.antibot.common.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("workflow API")
@RestController
@RequestMapping("/stream/workflow")
@Slf4j
@SuppressWarnings("all")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;


    @ApiOperation(value = "添加/修改工作流")
    @PostMapping(value = {"/add"})
    DataResponse<Boolean> add(@Validated @RequestBody AddWorkflowDTO addWorkflowDTO) {
        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            WorkflowBean workflowBean = ConvertUtil.convert(addWorkflowDTO,WorkflowBean.class);
            workflowBean.setType(WorkflowType.of(addWorkflowDTO.getType()).getCode());

            List<StreamVariableBean> variableBeanList = Lists.newArrayList();

            if (!CollectionUtils.isEmpty(addWorkflowDTO.getVariables())){
                variableBeanList = ConvertUtil.convert(addWorkflowDTO.getVariables(),streamVariableDTO->{
                    StreamVariableBean variableBean = ConvertUtil.convert(streamVariableDTO,StreamVariableBean.class);

                    variableBean.setType(VariableType.toType(streamVariableDTO.getType()).getCode());
                    variableBean.setValueType(DataType.of(streamVariableDTO.getValueType()).getCode());

                    if(VariableType.ROLLUP.getCode() == variableBean.getType()) {
                        variableBean.setSubType(RollupType.of(streamVariableDTO.getSubType()).getCode());
                    }
                    return variableBean;
                });
            }
            workflowService.addWorkflow(workflowBean,variableBeanList);
        } catch (Throwable t) {
            log.error("add workflow error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }

    @ApiOperation(value = "删除工作流")
    @DeleteMapping(value = {"/delete/{id}"})
    DataResponse<Boolean> delete(@PathVariable("id")Long id ){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{
            workflowService.deleteWorkflowById(id);
        }catch (Throwable t){
            log.error("delete workflow error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }

    @ApiOperation(value = "查询工作流详情")
    @GetMapping(value = "/getworkflow/{id}")
    public DataResponse<WorkflowDetailInfoDTO> getworkflow(@PathVariable("id")Long id ){

        DataResponse<WorkflowDetailInfoDTO> response = new DataResponse<>();

        try{

            WorkflowBean workflowBean = workflowService.getWorkflowById(id);
            if (workflowBean != null){
                WorkflowDetailInfoDTO workflowDetailInfoDTO = ConvertUtil.convert(workflowBean,WorkflowDetailInfoDTO.class);

                workflowDetailInfoDTO.setType(WorkflowType.of(workflowBean.getType()).getText());

                List<StreamVariableBean> streamVariableBeanList = workflowService.findVariableByWorkflowId(id);

                if (!CollectionUtils.isEmpty(streamVariableBeanList)){

                    List<StreamVariableDTO> streamVariableDTOList = ConvertUtil.convert(streamVariableBeanList,streamVariableBean -> {
                        StreamVariableDTO streamVariableDTO = ConvertUtil.convert(streamVariableBean,StreamVariableDTO.class);

                        streamVariableDTO.setType(VariableType.toType(streamVariableBean.getType()).getText());
                        if(VariableType.ROLLUP.getCode() == streamVariableBean.getType()) {
                            streamVariableDTO.setSubType(RollupType.of(streamVariableBean.getSubType()).getText());
                        }
                        streamVariableDTO.setValueType(DataType.of(streamVariableBean.getValueType()).get().getText());
                        return streamVariableDTO;
                    });
                    workflowDetailInfoDTO.setVariables(streamVariableDTOList);
                }

                response.setData(workflowDetailInfoDTO);
            }else {
                response.setCode(404);
                response.setResultMessage("工作流不存在");
            }
        }catch (Throwable t){
            log.error("get workflow detail info error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }

    @ApiOperation(value = "查询工作流列表")
    @GetMapping(value = "list")
    public PageListResponse<List<WorkflowDTO>> list(@ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize) {

        PageListResponse<List<WorkflowDTO>> pageResponse =  new PageListResponse<List<WorkflowDTO>>();
        try{

            Page<WorkflowBean> pageResult = workflowService.list(page,pageSize);

            List<WorkflowDTO> workflowDTOList = ConvertUtil.convert(pageResult.getResult(),workflowBean -> {
                WorkflowDTO workflowDTO = ConvertUtil.convert(workflowBean,WorkflowDTO.class);
                workflowDTO.setType(WorkflowType.of(workflowBean.getType()).getText());
                return workflowDTO;
            });
            pageResponse.setData(workflowDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        }catch (Throwable t){
            log.error("list workflow error",t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }


}
