package com.yjfei.antibot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    public static String formatDateToStr(String pattern,long timestamp){
        return new SimpleDateFormat(pattern).format(timestamp);
    }

    public static Long formatDateToLong(String pattern,long timestamp){
        return Long.parseLong(new SimpleDateFormat(pattern).format(timestamp));
    }

    public static Date format(String pattern,String dateStr) throws ParseException {

        return new SimpleDateFormat(pattern).parse(dateStr);
    }

    public static Date getDateByIntervalDay(int interval){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.add(Calendar.DAY_OF_MONTH, interval);

        return calendar.getTime();
    }


}
