package com.yjfei.antibot;

import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.dto.RiskRequestDTO;
import com.yjfei.antibot.dto.RiskResultDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "antibot-service", url = "${service.antibot-service}")
public interface RiskDetectClient {

    @ApiOperation(value = "风控决策调用")
    @PostMapping(value = "/api/antibot/detect")
    DataResponse<RiskResultDTO> detect(@RequestBody RiskRequestDTO request);
}
