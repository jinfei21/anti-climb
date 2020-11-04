package com.yjfei.antibot.controller;


import com.yjfei.antibot.bean.ModelBean;
import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.AddModel;
import com.yjfei.antibot.dto.ModelDTO;
import com.yjfei.antibot.dto.ModelDetailInfoDTO;
import com.yjfei.antibot.dto.ModelFieldDTO;
import com.yjfei.antibot.service.DataModelService;
import com.yjfei.antibot.util.ConvertUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("data model API")
@RestController
@RequestMapping("/model")
@Slf4j
@SuppressWarnings("all")
public class DataModelController {

    @Autowired
    private DataModelService dataModelService;


    @ApiOperation(value = "添加/修改数据模型")
    @PostMapping(value = {"/addmodel"})
    public DataResponse<Boolean> addModel(@Validated @RequestBody AddModel addModel) {

        DataResponse<Boolean> response = new DataResponse<>(true);
        try {
            ModelBean modelBean = ConvertUtil.convert(addModel, ModelBean.class);

            List<ModelFieldBean> modelFieldBeanList = null;
            if (!CollectionUtils.isEmpty(addModel.getFields())){

                modelFieldBeanList = ConvertUtil.convert(addModel.getFields(),modelFieldDTO -> {

                    ModelFieldBean modelFieldBean = ConvertUtil.convert(modelFieldDTO,ModelFieldBean.class);

                    modelFieldBean.setDataType(DataType.of(modelFieldDTO.getName()).getCode());

                    if (modelFieldBean.getRequired() == null){
                        modelFieldBean.setRequired(false);
                    }
                    return modelFieldBean;
                });

            }
            dataModelService.addModel(modelBean,modelFieldBeanList);
        } catch (Throwable t) {
            log.error("add datamodel error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }

        return response;

    }

    @ApiOperation(value = "删除模型")
    @DeleteMapping(value = {"/delete/{id}"})
    DataResponse<Boolean> delete(@PathVariable("id")Long id ){
        DataResponse<Boolean> response = new DataResponse<>(true);
        try{
            dataModelService.deleteDataModel(id);
        }catch (Throwable t){
            log.error("delete datasource error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }

    @ApiOperation(value = "查询模型详情")
    @GetMapping(value = "/getmodel/{id}")
    DataResponse<ModelDetailInfoDTO> getModelById(@PathVariable("id")Long id ){
        DataResponse<ModelDetailInfoDTO> response = new DataResponse<ModelDetailInfoDTO>();

        try{

            ModelBean modelBean = dataModelService.findModelById(id);

            if (modelBean != null){

                List<ModelFieldBean> modelFieldBeanList = dataModelService.findModelFieldByModelId(id);

                ModelDetailInfoDTO modelDetailInfoDTO = ConvertUtil.convert(modelBean,ModelDetailInfoDTO.class);

                if (!CollectionUtils.isEmpty(modelFieldBeanList)){

                    List<ModelFieldDTO> modelFieldDTOList = ConvertUtil.convert(modelFieldBeanList,modelFieldBean -> {
                        ModelFieldDTO modelFieldDTO = ConvertUtil.convert(modelFieldBean,ModelFieldDTO.class);

                        modelFieldDTO.setDataType(DataType.of(modelFieldBean.getDataType()).get().getText());
                        return modelFieldDTO;
                    });
                    modelDetailInfoDTO.setFields(modelFieldDTOList);
                }

                response.setData(modelDetailInfoDTO);
            }else {
                response.setCode(404);
                response.setResultMessage("模型不存在");
            }

        }catch (Throwable t){
            log.error("get model detail info error",t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }

    @ApiOperation(value = "查询列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<ModelDTO>> list(@ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize){

        PageListResponse<List<ModelDTO>> pageResponse = new PageListResponse<List<ModelDTO>>();

        try {

            Page<ModelBean> pageResult = dataModelService.list(page,pageSize);

            List<ModelDTO> modelDTOList = ConvertUtil.convert(pageResult.getResult(), modelBean -> {
                ModelDTO modelDTO = ConvertUtil.convert(modelBean, ModelDTO.class);
                return modelDTO;
            });


            pageResponse.setData(modelDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        }catch(Throwable t) {
            log.error("list datamodel error",t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }

        return pageResponse;
    }

}
