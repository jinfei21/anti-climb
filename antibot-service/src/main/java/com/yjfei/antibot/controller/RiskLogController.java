package com.yjfei.antibot.controller;


import com.yjfei.antibot.bean.DetectLogBean;
import com.yjfei.antibot.bean.DetectLogItemBean;
import com.yjfei.antibot.common.PageListResponse;
import com.yjfei.antibot.dto.DetectLogDTO;
import com.yjfei.antibot.dto.DetectLogItemDTO;
import com.yjfei.antibot.service.RiskLogService;
import com.yjfei.antibot.util.ConvertUtil;
import com.yjfei.antibot.util.DateUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api("risk log API")
@RestController
@RequestMapping("/risklog")
@Slf4j
@SuppressWarnings("all")
public class RiskLogController {

    @Autowired
    private RiskLogService riskLogService;


    @ApiOperation(value = "查询结果列表")
    @GetMapping(value = "/list")
    public PageListResponse<List<DetectLogDTO>> list(
            @ApiParam("策略ID") @RequestParam(value = "strategyId", required = false) Long strategyId,
            @ApiParam("风险等级") @RequestParam(value = "level", required = false) Integer level,
            @ApiParam("生效时间") @RequestParam(value = "start",required = false) @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss") Date start,
            @ApiParam("失效时间") @RequestParam(value = "end",required = false) @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss") Date end,
            @ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize) {

        PageListResponse<List<DetectLogDTO>> pageResponse = new PageListResponse<List<DetectLogDTO>>();
        try {

            if (start == null){
                start = DateUtil.getDateByIntervalDay(-30);
            }

            if (end == null){
                end = new Date();
            }

            Page<DetectLogBean> pageResult = riskLogService.list(strategyId,level,start,end,page,pageSize);

            List<DetectLogDTO> detectLogDTOList = ConvertUtil.convert(pageResult.getResult(), detectLogBean -> {
                DetectLogDTO detectLogDTO = ConvertUtil.convert(detectLogBean,DetectLogDTO.class);

                return detectLogDTO;
            });

            pageResponse.setData(detectLogDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());

        } catch (Throwable t) {
            log.error("list detectlog error", t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;
    }


    @ApiOperation(value = "查询结果列表详情")
    @GetMapping(value = "/listitem")
    public PageListResponse<List<DetectLogItemDTO>> listItem(
            @ApiParam("规则ID") @RequestParam(value = "ruleId", required = false) Long ruleId,
            @ApiParam("策略ID") @RequestParam(value = "strategyId", required = false) Long strategyId,
            @ApiParam("生效时间") @RequestParam(value = "start",required = false) @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss") Date start,
            @ApiParam("失效时间") @RequestParam(value = "end",required = false) @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss") Date end,
            @ApiParam("当前页") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("分页大小") @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize) {

        PageListResponse<List<DetectLogItemDTO>> pageResponse = new PageListResponse<List<DetectLogItemDTO>>();
        try {
            if (start == null){
                start = DateUtil.getDateByIntervalDay(-30);
            }

            if (end == null){
                end = new Date();
            }

            Page<DetectLogItemBean> pageResult = riskLogService.listitem(strategyId,ruleId,start,end,page,pageSize);

            List<DetectLogItemDTO> detectLogItemDTOList = ConvertUtil.convert(pageResult.getResult(),detectLogItemBean -> {
                DetectLogItemDTO detectLogItemDTO = ConvertUtil.convert(detectLogItemBean,DetectLogItemDTO.class);

                return detectLogItemDTO;
            });

            pageResponse.setData(detectLogItemDTOList);
            pageResponse.setTotalCount(pageResult.getTotal());
            pageResponse.setPage(pageResult.getPages());
            pageResponse.setPageSize(pageResult.getPageSize());
        } catch (Throwable t) {
            log.error("list detectlog item error", t);
            pageResponse.setCode(500);
            pageResponse.setResultMessage(t.getMessage());
        }
        return pageResponse;

    }


}
