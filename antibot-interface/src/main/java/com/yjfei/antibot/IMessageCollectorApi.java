package com.yjfei.antibot;

import com.yjfei.antibot.common.DataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "collector", description = "数据采集API")
@RequestMapping("/api/collector")
public interface IMessageCollectorApi {


    @ApiOperation(value = "采集数据")
    @PostMapping(value = "/send/{topic}")
    DataResponse<Boolean> send(@PathVariable("topic") String topic, @RequestBody Map<String,Object> message);

    @ApiOperation(value = "采集统计监控")
    @GetMapping(value = "/monitor")
    DataResponse<String> monitor();
}
