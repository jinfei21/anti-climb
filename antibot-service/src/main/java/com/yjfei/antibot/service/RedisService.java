package com.yjfei.antibot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class RedisService {

    @Autowired
    private  StringRedisTemplate redisTemplate;

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hget(String key){
        return redisTemplate.opsForHash().entries(key);
    }


    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
}


