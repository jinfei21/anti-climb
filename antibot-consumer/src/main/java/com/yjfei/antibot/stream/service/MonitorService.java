package com.yjfei.antibot.stream.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@SuppressWarnings("all")
public class MonitorService {

    private final ConcurrentHashMap<String, AtomicLong> successMap = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, AtomicLong> errorMap = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, AtomicLong> delayMap = new ConcurrentHashMap();


    private final AtomicLong delay = new AtomicLong(0);

    public long incrementSuccess(String metric){
        return increment(metric,successMap);
    }

    public long incrementError(String metric){
        return increment(metric,errorMap);
    }

    public long decrementError(String metric){
        return increment(metric,errorMap);
    }

    private long decrement(String metric,ConcurrentHashMap<String, AtomicLong> map){
        AtomicLong counter = map.get(metric);

        if (counter == null){
            counter = new AtomicLong(0);
            map.put(metric,counter);
        }
        return counter.decrementAndGet();
    }

    private long increment(String metric,ConcurrentHashMap<String, AtomicLong> map){
        AtomicLong counter = map.get(metric);

        if (counter == null){
            counter = new AtomicLong(0);
            map.put(metric,counter);
        }
        return counter.incrementAndGet();
    }

    public void setDelay(String metric,long d){
        AtomicLong delay = delayMap.get(metric);

        if (delay == null){
            delay = new AtomicLong(0);
            delayMap.put(metric,delay);
        }
        delay.set(d);
    }


    public String getStatResult(){
        Map<String,Object> map = new HashMap<>();

        map.put("success",successMap);
        map.put("error",errorMap);
        map.put("delay",delayMap);

        return map.toString();
    }
}
