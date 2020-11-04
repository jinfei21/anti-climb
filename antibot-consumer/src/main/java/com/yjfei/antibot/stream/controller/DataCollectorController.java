package com.yjfei.antibot.stream.controller;

import com.yjfei.antibot.IMessageCollectorApi;
import com.yjfei.antibot.common.DataResponse;
import com.yjfei.antibot.stream.config.AliyunOnsProperties;
import com.yjfei.antibot.stream.service.MonitorService;
import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Properties;

@RestController
@Slf4j
@SuppressWarnings("all")
@Scope(value = "singleton")
public class DataCollectorController implements IMessageCollectorApi {

    @Value("${ons.tag.antibot}")
    private String tag;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private ApplicationContext applicationContext;

    private Producer producer;

    @PostConstruct
    private void init(){
        AliyunOnsProperties aliyunOnsProperties = applicationContext.getBean(AliyunOnsProperties.class);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, aliyunOnsProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, aliyunOnsProperties.getSecretKey());
        if (!StringUtils.isEmpty(aliyunOnsProperties.getNameServerAddress())) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR, aliyunOnsProperties.getNameServerAddress());
        }
        this.producer = ONSFactory.createProducer(properties);
        this.producer.start();
    }



    @Override
    public DataResponse<Boolean> send(String topic, Map<String,Object> message) {
        DataResponse<Boolean> response = new DataResponse<Boolean>(true);
        try {

            com.aliyun.openservices.ons.api.Message msg = new com.aliyun.openservices.ons.api.Message();
            msg.setTopic(topic);
            msg.setTag(tag);
            message.put("timestamp",String.valueOf(System.currentTimeMillis()));
            msg.setBody(JSON.toJSONString(message).getBytes());
            producer.send(msg);
            log.error("<<<<<<topic:{}",topic);
        }catch (Throwable t){
            log.error("publish message fail!",t);
            log.error(JSON.toJSONString(applicationContext.getBean(AliyunOnsProperties.class)));
            response.setData(false);
            response.setResultMessage(t.getMessage());
            response.setCode(500);
        }
        return response;
    }

    @Override
    public DataResponse<String> monitor() {

        DataResponse<String> response = new DataResponse<String>();
        response.setData(monitorService.getStatResult());
        return response;
    }
}
