package com.yjfei.antibot.stream.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class VariableDao {

    @Autowired
    private RedisTemplate redisTemplate;


    public void incrementHset(String key,Object hk,Object value,Long expiretime){
        log.error(key+":"+hk+":"+value);
        if (value.getClass().isAssignableFrom(Long.class)) {
            redisTemplate.opsForHash().increment(key, hk, (long)value);
        }else{
            redisTemplate.opsForHash().increment(key, hk, (double)value);
        }
        redisTemplate.expire(key,expiretime, TimeUnit.MINUTES);
    }

    public void addSet(String key,Object value, Long expiretime){
        redisTemplate.opsForSet().add(key,value);
        redisTemplate.expire(key,expiretime, TimeUnit.MINUTES);

    }

    public void set(String key, Object value, Long expiretime){
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key,expiretime, TimeUnit.MINUTES);

    }


}
