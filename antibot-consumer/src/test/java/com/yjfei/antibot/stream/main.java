package com.yjfei.antibot.stream;

import com.yjfei.antibot.data.Message;
import com.yjfei.antibot.stream.engine.Context;
import com.yjfei.antibot.stream.engine.Expression;
import com.yjfei.antibot.util.DateUtil;
import com.alibaba.fastjson.JSON;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class main {

    public static final long ONE_MINUTE = 60 * 1000L;
    public static final long ONE_HOUR = 60 * 60 * 1000L;
    public static final long ONE_DAY = 24*60 * 60 * 1000L;
    public static final long ONE_MONTH = 30*24*60 * 60 * 1000L;

    public static void main(String args[]){

        Message msg = new Message();

        msg.setTimestamp(System.currentTimeMillis());
        msg.put("a","b");
        System.out.println(JSON.toJSONString(msg));


        String s = "format(\"yyyy-MM-dd HH:mm:ss\", message.dt)";
        Expression expression = new Expression(s);


        msg.put("dt", "2020-01-22 11:15:27");
        Context context = new Context();
        context.put("dt", "2020-01-22 11:15:27");
        context.put("message",msg);

        Object result = expression.calculate(context);
        System.out.println(result);

        System.out.println(DateUtil.formatDateToStr("yyyyMMDDHHmm",System.currentTimeMillis()));
        System.out.println(DateUtil.formatDateToLong("yyyyMMDDHHmm",System.currentTimeMillis() - ONE_HOUR));

        LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());


        System.out.println("key"+currentTime.getYear() + currentTime.getDayOfYear() + currentTime.getHour());
        System.out.println("key" + currentTime.getYear() + currentTime.getMonthValue());
        System.out.println(String.format("%s%s%s%s","key",currentTime.getYear() , currentTime.getDayOfYear() , currentTime.getHour()));

        System.out.println("===");
        LocalDateTime currentTime1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() - ONE_MONTH-1), ZoneId.systemDefault());

        System.out.println("key"+currentTime1.getYear() + currentTime1.getDayOfYear() + currentTime1.getHour());
        System.out.println("key" + currentTime1.getYear() + currentTime1.getMonthValue()+currentTime1.getDayOfMonth());

        System.out.println(currentTime.getDayOfMonth());
        System.out.println(currentTime1.getDayOfMonth());

        long l  = System.currentTimeMillis() + ONE_HOUR;

        System.out.println(Long.parseLong("0010"));
        while (true){
             l  += ONE_MINUTE;
            currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault());
            System.out.println(currentTime.getMinute());
        }

    }

}
