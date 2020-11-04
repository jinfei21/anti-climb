package com.yjfei.antibot;


import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.dto.RiskResultDTO;
import com.yjfei.antibot.dto.RiskRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "risk", description = "风险识别API")
@RequestMapping("/api/antibot")
public interface IRiskDetectApi {

    @ApiOperation(value = "风控决策调用")
    @PostMapping(value = "/detect")
    DataResponse<RiskResultDTO> detect(@RequestBody RiskRequestDTO request);
}
