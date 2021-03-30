package com.yjfei.antibot.engine.variable;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.config.ApplicationContextProvider;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.Expression;
import com.yjfei.antibot.service.RedisService;
import com.yjfei.antibot.service.StreamVariableService;
import com.yjfei.antibot.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TumbleVariable extends RollupVariable<Number> {


    private static final long ONE_HOUR = 60 * 60 * 1000L;
    private static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    private static final long ONE_MONTH = 30 * 24 * 60 * 60 * 1000L;
    /**
     * 查找的key变量
     */
    private Expression keyExpression;

    /**
     * 对应的数据变量ID
     */
    private Long rawVariableId;

    /**
     * 时间
     */
    private Integer period;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 数据类型
     */
    private DataType dataType;

    public TumbleVariable(VariableBean variableBean) {
        super(variableBean);
        this.rawVariableId = variableBean.getSourceId();
        this.keyExpression = new Expression(variableBean.getKeyExpression());
        this.period = variableBean.getPeriod();
        if (Constants.HOUR.equals(variableBean.getTimeUnit())) {
            this.timeUnit = TimeUnit.HOURS;
        } else if (Constants.DAY.equals(variableBean.getTimeUnit())) {
            this.timeUnit = TimeUnit.DAYS;
        } else if (Constants.MINUTE.equals(variableBean.getTimeUnit())) {
            this.timeUnit = TimeUnit.MINUTES;
        } else {
            throw new IllegalArgumentException("不支持的time unit：" + variableBean.getTimeUnit());
        }

        StreamVariableService streamVariableService = ApplicationContextProvider.getApplicationContext().getBean(StreamVariableService.class);
        StreamVariableBean streamVariableBean = streamVariableService.getVariableById(variableBean.getSourceId());

        this.dataType = DataType.of(streamVariableBean.getValueType()).orElseThrow(() -> new IllegalArgumentException("不支持的数据类型：" + streamVariableBean.getValueType()));
    }


    @Override
    protected Number evaluate(Context context) {

        //TODO查询聚合
        Object keyObject = keyExpression.calculate(context);

        double result = 0D;

        String key = buildPrimaryKey(rawVariableId.toString(), keyObject.toString());
        String startKey = null;
        String endKey = null;
        LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(context.getTimestamp()), ZoneId.systemDefault());
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(context.getTimestamp() - this.timeUnit.toMillis(this.period)), ZoneId.systemDefault());

        switch (timeUnit) {
            case MINUTES:

                startKey = String.format("%s%s%s%s", key, currentTime.getYear(), currentTime.getDayOfYear(), currentTime.getHour());
                LocalDateTime preTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(context.getTimestamp() - ONE_HOUR), ZoneId.systemDefault());
                endKey = String.format("%s%s%s%s", key, preTime.getYear(), preTime.getDayOfYear(), preTime.getHour());
                long startIndex = Long.valueOf(String.format("%s%s", startTime.getHour(), formatNum(startTime.getMinute())));

                long endIndex = Long.valueOf(String.format("%s%s", currentTime.getHour(), formatNum(currentTime.getMinute())));

                result = calculate(startKey, endKey, startIndex, endIndex, Constants.MAX_MINUTES);
                break;
            case HOURS:

                startKey = String.format("%s%s%s", key, currentTime.getYear(), currentTime.getDayOfYear());
                preTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(context.getTimestamp() - ONE_DAY), ZoneId.systemDefault());
                endKey = String.format("%s%s%s", key, preTime.getYear(), preTime.getDayOfYear());

                startIndex = Long.valueOf(String.format("%s%s", startTime.getDayOfYear(), formatNum(startTime.getHour())));

                endIndex = Long.valueOf(String.format("%s%s", currentTime.getDayOfYear(), formatNum(currentTime.getHour())));
                result = calculate(startKey, endKey, startIndex, endIndex, Constants.MAX_HOURS);
                break;
            case DAYS:

                startKey = String.format("%s%s%s", key, currentTime.getYear(), currentTime.getMonthValue());
                preTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(context.getTimestamp() - ONE_MONTH), ZoneId.systemDefault());
                endKey = String.format("%s%s%s", key, preTime.getYear(), preTime.getMonthValue());
                startIndex = Long.valueOf(String.format("%s%s", startTime.getMonthValue(), formatNum(startTime.getDayOfMonth())));

                endIndex = Long.valueOf(String.format("%s%s", currentTime.getMonthValue(), formatNum(currentTime.getDayOfMonth())));

                result = calculate(startKey, endKey, startIndex, endIndex, Constants.MAX_DAYS);
                break;
            default:
                break;
        }


        return result;
    }

    private String formatNum(Integer num){
        if (num < 10){
            return "0"+num;
        }else {
            return num.toString();
        }
    }

    private double calculate(String startKey, String endKey, long startIndex, long endIndex, int maxPeriod) {
        if (period > maxPeriod) {
            log.error("time can't be larger than {} when unit is {}", maxPeriod, timeUnit);
            return 0;
        }



        RedisService redisService = ApplicationContextProvider.getApplicationContext().getBean(RedisService.class);

        Map<Object, Object> startMap = redisService.hget(startKey);

        double sum = sum(startMap, startIndex, endIndex);

        if (!startKey.equalsIgnoreCase(endKey)) {
            Map<Object, Object> endMap = redisService.hget(endKey);
            sum = sum + sum(endMap, startIndex, endIndex);
        }

        return sum;
    }

    private double sum(Map<Object, Object> map, long startIndex, long endIndex) {
        if (CollectionUtils.isEmpty(map)) {
            return 0D;
        }
        double sum = 0;

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            long lkey = Long.parseLong(entry.getKey().toString());

            if (lkey >= startIndex && lkey <= endIndex) {
                double dValue = Double.parseDouble(entry.getValue().toString());
                sum = sum + dValue;
            }
        }
        return sum;
    }

    private String buildPrimaryKey(String prefix, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(key);
        return sb.toString();
    }
}
