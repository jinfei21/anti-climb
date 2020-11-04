package com.yjfei.antibot.api;

import com.yjfei.antibot.IRiskDetectApi;
import com.yjfei.antibot.MessageCollectorClient;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.data.DataModel;
import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.service.RiskDetectService;
import com.yjfei.antibot.dto.RiskResultDTO;
import com.yjfei.antibot.dto.RiskRequestDTO;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api("风控决策调用API")
@RestController
@Slf4j
@SuppressWarnings("all")
public class RiskDetectController implements IRiskDetectApi {

    @Autowired
    private RiskDetectService riskDetectService;

    @Autowired
    private MessageCollectorClient collectorClient;

    private Config config = ConfigService.getAppConfig();

    public DataResponse<RiskResultDTO> detect(@Valid @RequestBody RiskRequestDTO request) {
        DataResponse<RiskResultDTO> response = new DataResponse<RiskResultDTO>();

        try{

            if (config.getBooleanProperty("antibot.collect.enable",false)){
                collectorClient.send(config.getProperty("antibot."+request.getStrategyCode()+".topic","topic_fusion_antibot"),request.getData());
            }

            DataModel dataModel = riskDetectService.getDataModelByCode(request.getStrategyCode());

            if (dataModel == null){
                response.setCode(500);
                response.setResultMessage("strategyCode is not existed.");
                log.warn("strategyCode:"+request.getStrategyCode()+" is not existed.");
                return response;
            }

            DataSet dataSet = dataModel.cast(request.getData());

            dataSet.validate();

            RiskResultDTO result = riskDetectService.detect(request.getStrategyCode(),dataSet);

            response.setData(result);

        }catch (Throwable t){
            response.setCode(500);
            response.setResultMessage(t.getMessage());
            log.error("strategyCode:{} detect error,{}",request.getStrategyCode(),t);
        }

        return response;
    }
}
