package com.yjfei.antibot.stream.service;


import com.yjfei.antibot.common.RollupType;
import com.yjfei.antibot.stream.dao.VariableDao;
import com.yjfei.antibot.stream.engine.VariableValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
@Slf4j
public class TumbleVariableService extends RollupVariableService {
    private static final long ONE_HOUR = 60 * 60 * 1000L;
    private static final long TWO_HOUR = 2 * 60;
    private static final long TWO_DAY = 60 * 24 * 2;
    private static final long TWO_MONTH = 60 * 24 * 30 * 2;


    @Autowired
    public TumbleVariableService(VariableDao variableDao) {
        super(variableDao, RollupType.TUMBLE_WINDOW);
    }

    @Override
    public void save(long timestamp, List<VariableValue> variableValueList) {
        if (Math.abs(System.currentTimeMillis() - timestamp) > ONE_HOUR) {
            // 对于延迟到达超过60分钟的数据不再进行处理，对于时间超过当前60分钟的数据认为是错误数据，不再进行处理
            return;
        }

        Map<String, VariableValue> variableValueMap = groupByKey(timestamp, variableValueList);

        variableValueMap.forEach((key, variableValue) -> {



            LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

            //TODO 插入分钟
            variableDao.incrementHset(String.format("%s%s%s%s", key, currentTime.getYear(), currentTime.getDayOfYear(), currentTime.getHour()), String.format("%s%s", currentTime.getHour(), formatNum(currentTime.getMinute())), variableValue.getValue(), TWO_HOUR);

            //TODO 插入小时
            variableDao.incrementHset(String.format("%s%s%s", key, currentTime.getYear(), currentTime.getDayOfYear()), String.format("%s%s", currentTime.getDayOfYear(), formatNum(currentTime.getHour())), variableValue.getValue(), TWO_DAY);

            //TODO 插入天
            variableDao.incrementHset(String.format("%s%s%s", key, currentTime.getYear(), currentTime.getMonthValue()), String.format("%s%s", currentTime.getMonthValue(), formatNum(currentTime.getDayOfMonth())), variableValue.getValue(), TWO_MONTH);
        
        });

    }

    private String formatNum(Integer num){
        if (num < 10){
            return "0"+num;
        }else {
            return num.toString();
        }
    }


    Map<String, VariableValue> groupByKey(long timestamp, List<VariableValue> variableValueList) {
        Map<String, VariableValue> variableValueMap = new HashMap<>(variableValueList.size());

        for (VariableValue variableValue : variableValueList) {
            if (variableValue.isEmpty()) {
                continue;
            }
            String key = buildPrimaryKey(variableValue.getBean().getId().toString(), variableValue.getKey());

            if (!variableValueMap.containsKey(key)) {
                variableValueMap.put(key, variableValue);
            } else {
                variableValueMap.computeIfPresent(key, (pk, old) -> {
                    Object resultValue;
                    if (Long.class.isAssignableFrom(old.getValue().getClass())) {
                        resultValue = (Long) old.getValue() + (Long) variableValue.getValue();
                    } else {
                        resultValue = ((BigDecimal) old.getValue()).add((BigDecimal) variableValue.getValue()).doubleValue();
                    }
                    return new VariableValue(old.getBean(), old.getKey(), resultValue, timestamp);
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
