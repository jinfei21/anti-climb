package com.yjfei.antibot.controller;


import com.yjfei.antibot.bean.DataSourceBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.DataSourceType;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.AddDatasourceDTO;
import com.yjfei.antibot.dto.DataSourceDTO;
import com.yjfei.antibot.service.DataSourceService;
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

@Api("data source API")
@RestController
@RequestMapping("/stream/datasource")
@Slf4j
@SuppressWarnings("all")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @ApiOperation(value = "添加/修改数据源")
    @PostMapping(value = {"/add"})
    DataResponse<Boolean> add(@Validated @RequestBody AddDatasourceDTO addDatasourceDTO){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{

            DataSourceBean dataSourceBean = ConvertUtil.convert(addDatasourceDTO,DataSourceBean.class);

            dataSourceBean.setType(DataSourceType.of(addDatasourceDTO.getType()).getCode());
            dataSourceService.addDataSource(dataSourceBean);

        }catch (Throwable t){
            log.error("add datasource error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;
    }

    @ApiOperation(value = "删除数据源")
    @DeleteMapping(value = {"/delete/{id}"})
    DataResponse<Boolean> delete(@PathVariable("id")Long id ){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{

            dataSourceService.deleteDataSourceById(id);
        }catch (Throwable t){
            log.error("delete datasource error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }


    @ApiOperation(value = "查询数据库列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<DataSourceDTO>> list(@ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize){

        PageListResponse<List<DataSourceDTO>> pageResponse =  new PageListResponse<List<DataSourceDTO>>();
        try{

            Page<DataSourceBean> pageResult =  dataSourceService.list(page,pageSize);

            List<DataSourceDTO> dataSourceDTOList = ConvertUtil.convert(pageResult.getResult(),dataSourceBean -> {
                DataSourceDTO dataSourceDTO = ConvertUtil.convert(dataSourceBean,DataSourceDTO.class);
                dataSourceDTO.setType(DataSourceType.of(dataSourceBean.getType()).getText());
                return dataSourceDTO;
            });

            pageResponse.setData(dataSourceDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());

        }catch (Throwable t){
            log.error("list datasource error",t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }


}
