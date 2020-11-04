package com.yjfei.antibot.controller;

import com.yjfei.antibot.bean.NameListBean;
import com.yjfei.antibot.bean.NameListItemBean;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.common.NameListType;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.AddNameListDTO;
import com.yjfei.antibot.dto.AddNameListItemDTO;
import com.yjfei.antibot.dto.NameListDTO;
import com.yjfei.antibot.dto.NameListItemDTO;
import com.yjfei.antibot.service.NameListService;
import com.yjfei.antibot.util.ConvertUtil;
import com.yjfei.antibot.util.DateUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api("namelist API")
@RestController
@RequestMapping("/namelist")
@Slf4j
@SuppressWarnings("all")
public class NameListController {


    @Autowired
    private NameListService nameListService;

    @ApiOperation(value = "添加/修改名单列表")
    @PostMapping(value = {"/add"})
    public DataResponse<Boolean> add(@Validated @RequestBody AddNameListDTO addNameListDTO) {
        DataResponse<Boolean> response = new DataResponse<>();
        try {

            NameListBean nameListBean = ConvertUtil.convert(addNameListDTO, NameListBean.class);
            nameListService.addNameList(nameListBean);

        } catch (Throwable t) {
            log.error("add namelist error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }

    @ApiOperation(value = "删除名单列表")
    @DeleteMapping(value = {"/delete/{id}"})
    public DataResponse<Boolean> delete(@PathVariable("id") Long id) {
        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            nameListService.deleteNameList(id);
        } catch (Throwable t) {
            log.error("delete namelist error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }

    @ApiOperation(value = "添加/修改名单item")
    @PostMapping(value = {"/additem"})
    public DataResponse<Boolean> additem(@Validated @RequestBody AddNameListItemDTO addNameListItemDTO) {

        DataResponse<Boolean> response = new DataResponse<>(true);
        try {

            NameListItemBean nameListItemBean = ConvertUtil.convert(addNameListItemDTO, NameListItemBean.class);
            nameListItemBean.setType(NameListType.of(addNameListItemDTO.getType()).getCode());
            nameListService.addNameListItem(nameListItemBean);
        } catch (Throwable t) {
            log.error("add namelist item error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;
    }


    @ApiOperation(value = "删除名单列表")
    @DeleteMapping(value = {"/deleteitem/{id}"})
    public DataResponse<Boolean> deleteitem(@PathVariable("id") Long id) {


        DataResponse<Boolean> response = new DataResponse<>(true);
        try {
            nameListService.deleteNameListItem(id);
        } catch (Throwable t) {
            log.error("delete namelist item error", t);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
            response.setData(false);
        }
        return response;

    }


    @ApiOperation(value = "查询名单列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<NameListDTO>> list(@ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize) {

        PageListResponse<List<NameListDTO>> pageResponse = new PageListResponse<List<NameListDTO>>();

        try {

            Page<NameListBean> pageResult = nameListService.listNameList(page,pageSize);

            List<NameListDTO> nameListDTOList = ConvertUtil.convert(pageResult.getResult(),nameListBean -> {
                NameListDTO nameListDTO = ConvertUtil.convert(nameListBean,NameListDTO.class);

                return nameListDTO;
            });

            pageResponse.setData(nameListDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        } catch (Throwable t) {
            log.error("list namelist error", t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }

    @ApiOperation(value = "查询名单列表详情")
    @GetMapping(value = "/listitem")
    public PageListResponse<List<NameListItemDTO>> listitem(
            @ApiParam("查询key") @RequestParam(value = "key", defaultValue = "") String key,
            @ApiParam("名单类型") @RequestParam(value = "type", defaultValue = "") String type,
            @ApiParam("生效时间") @RequestParam(value = "start",required = false) @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss") Date start,
            @ApiParam("失效时间") @RequestParam(value = "end", required = false) @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss") Date end,
            @ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize) {

        PageListResponse<List<NameListItemDTO>> pageResponse = new PageListResponse<List<NameListItemDTO>>();

        try {

            if (StringUtils.isEmpty(key)){
                key = null;
            }

            Integer nameListType = null;

            if (!StringUtils.isEmpty(type)){
                nameListType = NameListType.of(type).getCode();
            }

            if (start == null){
                start = DateUtil.getDateByIntervalDay(-30);
            }

            if (end == null){
                end = new Date();
            }

            Page<NameListItemBean> pageResult = nameListService.listNameListItem(key,nameListType,start,end,page,pageSize);

            List<NameListItemDTO> nameListItemDTOList = ConvertUtil.convert(pageResult.getResult(),nameListItemBean -> {
                NameListItemDTO nameListItemDTO = ConvertUtil.convert(nameListItemBean,NameListItemDTO.class);
                nameListItemDTO.setType(NameListType.of(nameListItemBean.getType()).getText());
                return nameListItemDTO;
            });

            pageResponse.setData(nameListItemDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());

        } catch (Throwable t) {
            log.error("list namelist item error", t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }
}
