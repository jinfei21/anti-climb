package com.yjfei.antibot;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "antibot-consumer", url = "${service.antibot-consumer}")
public interface MessageCollectorClient extends IMessageCollectorApi {

}
