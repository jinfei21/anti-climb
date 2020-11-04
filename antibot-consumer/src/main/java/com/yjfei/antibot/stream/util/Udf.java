package com.yjfei.antibot.stream.util;

import com.yjfei.antibot.data.Message;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Udf {
    private static final long ONE = 1L;

    public long cnt() {
        return ONE;
    }

    public long cnt(int value) {
        return value;
    }

    public BigDecimal sum(BigDecimal value) {
        return value;
    }

    public long format(String format, String date) throws ParseException {
        Date time = new SimpleDateFormat(format).parse(date);
        return time.getTime();
    }

    public List<Message> flatBy(Message data, String fieldName) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> innerDataList = (List<Map<String, Object>>) data.get(fieldName);
        List<Message> messageList = innerDataList.stream()
                .map(Message::new)
                .collect(Collectors.toList());
        messageList.forEach(message -> message.put("__outer__", new HashMap<>(data)));
        return messageList;
    }

}