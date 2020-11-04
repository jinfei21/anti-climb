package com.yjfei.antibot.controller;



import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.AddStreamVariableDTO;
import com.yjfei.antibot.dto.StreamVariableDTO;
import com.yjfei.antibot.service.StreamVariableService;
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

@Api("streamvariable API")
@RestController
@RequestMapping("/stream/variable")
@Slf4j
@SuppressWarnings("all")
public class StreamVariableController {


    @Autowired
    private StreamVariableService streamVariableService;


    @ApiOperation(value = "添加/修改流变量")
    @PostMapping(value = {"/add"})
    DataResponse<Boolean> add(@Validated @RequestBody AddStreamVariableDTO addStreamVariableDTO){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{

            StreamVariableBean streamVariableBean = ConvertUtil.convert(addStreamVariableDTO,StreamVariableBean.class);

            streamVariableService.addStreamVariable(streamVariableBean);

        }catch (Throwable t){
            log.error("add streamvariable error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }


    @ApiOperation(value = "删除流变量")
    @DeleteMapping(value = {"/delete/{id}"})
    DataResponse<Boolean> delete(@PathVariable("id")Long id ){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{
            streamVariableService.deleteStreamVariableById(id);
        }catch (Throwable t){
            log.error("delete streamvariable error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }


    @ApiOperation(value = "查询流变量列表")
    @GetMapping(value = "list")
    public PageListResponse<List<StreamVariableDTO>> list(@ApiParam("workflowId") @RequestParam(value = "workflowId",required = false) Long workflowId,
                                                          @ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize) {

        PageListResponse<List<StreamVariableDTO>> pageResponse = new PageListResponse<List<StreamVariableDTO>>();
        try {

            Page<StreamVariableBean> pageResult = streamVariableService.list(workflowId,page,pageSize);

            List<StreamVariableDTO> streamVariableDTOList = ConvertUtil.convert(pageResult.getResult(),streamVariableBean -> {
                StreamVariableDTO streamVariableDTO = ConvertUtil.convert(streamVariableBean,StreamVariableDTO.class);

                return streamVariableDTO;
            });
            pageResponse.setData(streamVariableDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());

        } catch (Throwable t) {
            log.error("list streamvariable error", t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }



}
