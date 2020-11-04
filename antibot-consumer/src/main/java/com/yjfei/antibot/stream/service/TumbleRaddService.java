package com.yjfei.antibot.stream.service;


import com.yjfei.antibot.common.RollupType;
import com.yjfei.antibot.stream.dao.VariableDao;
import com.yjfei.antibot.stream.engine.VariableValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class TumbleRaddService extends RollupVariableService {
    private static final long ONE_HOUR = 60 * 60 * 1000L;
    private static final long TWO_HOUR = 2 * 60;
    private static final long TWO_DAY = 60 * 24 * 2;
    private static final long TWO_MONTH = 60 * 24 * 30 * 2;


    @Autowired
    public TumbleRaddService(VariableDao variableDao) {
        super(variableDao, RollupType.TUMBLE_RADD);
    }

    @Override
    public void save(long timestamp, List<VariableValue> variableValueList) {
        if (Math.abs(System.currentTimeMillis() - timestamp) > ONE_HOUR) {
            // 对于延迟到达超过60分钟的数据不再进行处理，对于时间超过当前60分钟的数据认为是错误数据，不再进行处理
            return;
        }

        Map<String, Map<String,Integer>> variableValueMap = groupByKey(variableValueList);

        variableValueMap.forEach((key, variableValue) -> {


            LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

            for(Map.Entry<String,Integer> entry:variableValue.entrySet()){

                //TODO 插入分钟
                variableDao.incrementHset(String.format("%s%s%s%s", key, currentTime.getYear(), currentTime.getDayOfYear(), currentTime.getHour()),entry.getKey(),entry.getValue(), TWO_HOUR);

                //TODO 插入小时
                variableDao.incrementHset(String.format("%s%s%s", key, currentTime.getYear(), currentTime.getDayOfYear()),entry.getKey(),entry.getValue(),TWO_DAY);

                //TODO 插入天
                variableDao.incrementHset(String.format("%s%s%s", key, currentTime.getYear(), currentTime.getMonthValue()),entry.getKey(),entry.getValue(),TWO_MONTH);

            }

        });

    }


    Map<String, Map<String,Integer>> groupByKey( List<VariableValue> variableValueList) {
        Map<String, Map<String,Integer>> variableValueMap = new HashMap<>(variableValueList.size());

        for (VariableValue variableValue : variableValueList) {
            if (variableValue.isEmpty()) {
                continue;
            }
            String key = buildPrimaryKey(variableValue.getBean().getId().toString(), variableValue.getKey());

            if (!variableValueMap.containsKey(key)) {
                Map<String,Integer> valueMap = new HashMap<String,Integer>();
                valueMap.put(String.valueOf(variableValue.getValue()),1);
                variableValueMap.put(key, valueMap);
            } else {
                variableValueMap.computeIfPresent(key, (pk, old) -> {
                    Map<String,Integer> oldMap = (Map<String,Integer>)old;

                    oldMap.computeIfPresent(pk,(vpk,vold)->{

                        return 1+vold;
                    });
                    return oldMap;
                });
            }
        }

        return variableValueMap;
    }

    private String buildPrimaryKey(String prefix, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(key);
        return sb.toString();
    }
}
