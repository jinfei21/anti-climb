package com.yjfei.antibot.stream.datasource;

import com.yjfei.antibot.bean.DataSourceBean;
import com.yjfei.antibot.data.Message;
import com.yjfei.antibot.stream.config.AliyunOnsProperties;
import com.yjfei.antibot.stream.engine.DataSource;
import com.yjfei.antibot.stream.engine.MessageHandler;
import com.yjfei.antibot.stream.service.MonitorService;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * 阿里云ONS MQ类型的数据源
 */
@Slf4j
public class ONSDataSource implements DataSource {

    private static final Type DESERIALIZABLE_TYPE = new TypeToken<Map<String, Object>>() {}.getType();

    private final Gson gson = new GsonBuilder().create();

    private final Consumer consumer;
    private final String topic;
    private final String tag;

    private MonitorService monitorService;

    public ONSDataSource(ApplicationContext applicationContext, DataSourceBean dataSourceBean) {
        this.monitorService = applicationContext.getBean(MonitorService.class);

        AliyunOnsProperties aliyunOnsProperties = applicationContext.getBean(AliyunOnsProperties.class);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey,
                getConfigValue(dataSourceBean.getUsername(),aliyunOnsProperties.getAccessKey()));
        properties.put(PropertyKeyConst.SecretKey,
                getConfigValue(dataSourceBean.getPassword(), aliyunOnsProperties.getSecretKey()));

        if (!StringUtils.isEmpty(dataSourceBean.getAddress())) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR,
                    getConfigValue(dataSourceBean.getAddress(), aliyunOnsProperties.getNameServerAddress()));
        }
        properties.put(PropertyKeyConst.GROUP_ID, "GID_fusion_antibot_"+dataSourceBean.getId());
        properties.put(PropertyKeyConst.ConsumeThreadNums, 1);

        consumer = ONSFactory.createConsumer(properties);

        this.topic = dataSourceBean.getGroup();
        this.tag = dataSourceBean.getSubGroup();

        log.info("ONS Consumer create success,topic:{},tag:{},groupId:{}",this.topic,this.tag,"GID_fusion_antibot_"+dataSourceBean.getId());
    }
    private String getConfigValue(String rawConfigValue, String dbConfigValue) {
        return StringUtils.isEmpty(dbConfigValue) ? rawConfigValue : dbConfigValue;
    }

    @Override
    public void connectWith(MessageHandler messageHandler) {

        consumer.subscribe(topic,tag,(msg,context) -> {
            String metric = topic+"-"+tag;

            log.error(">>>>>>>>>>consumer:"+metric);
            try{
                String body = new String(msg.getBody(), StandardCharsets.UTF_8);

                log.debug("consume new message,id is {},reconsume time is {},key is {},body is {}",
                        msg.getMsgID(),msg.getReconsumeTimes(),msg.getKey(),body);
                Map<String, Object> data = gson.fromJson(body, DESERIALIZABLE_TYPE);

                Message message = new Message(data);
                message.setId(msg.getMsgID());
                message.setTimestamp(data.get("timestamp")==null?System.currentTimeMillis():Long.parseLong(data.get("timestamp").toString()));

                monitorService.setDelay(metric,System.currentTimeMillis() - message.getTimestamp());

                if (msg.getReconsumeTimes() > 0) {
                    monitorService.decrementError(metric);
                }

                boolean result = messageHandler.handle(message);

                if (result) {
                    monitorService.incrementSuccess(metric);
                    return Action.CommitMessage;
                } else {
                    monitorService.incrementError(metric);
                    return Action.ReconsumeLater;
                }

            }catch (Exception e){
                monitorService.incrementError(metric);
                log.error("handle message error!",e);
                return Action.ReconsumeLater;
            }
        });
    }

    @Override
    public void start() {
        if(consumer != null){
            consumer.start();
        }
        log.info("start ONS MQ Consumer,topic:{},tag:{}",this.topic,this.tag);
    }

    @Override
    public void stop() {

        if (consumer != null){
            consumer.shutdown();
        }
        log.info("stop ONS MQ Consumer,topic is {},tag is {} ",this.topic,this.tag);

    }

    @Override
    public boolean isRunning() {
        return consumer.isStarted();
    }
}
