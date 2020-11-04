package com.yjfei.antibot.util;

import com.yjfei.antibot.config.ApplicationContextProvider;
import com.yjfei.antibot.data.Message;
import com.yjfei.antibot.engine.model.DataSet;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Udf {

    public List<DataSet> flatBy(DataSet original, String fieldName) {
        List<DataSet> innerDataList = original.get(fieldName);
        for (DataSet dataSet : innerDataList) {
            original.forEach((key, value) -> {
                if (!dataSet.containsKey(key) && !fieldName.equals(key)) {
                    dataSet.put(key, value);
                }
            });
        }
        return innerDataList;
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

    public Object call(String clientName, String operationName, Object... params) {
        Object result = null;
        try {
            Object client = ApplicationContextProvider.getApplicationContext().getBean(clientName);
            Method operation = Arrays.stream(client.getClass().getMethods()).filter(method -> {
                if (!operationName.equals(method.getName())) {
                    return false;
                }
                if (method.getParameterCount() != params.length) {
                    return false;
                }
                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] != null && !params[i].getClass().getSimpleName()
                                .equalsIgnoreCase(method.getParameterTypes()[i].getSimpleName())) {
                            return false;
                        }
                    }
                }
                return true;
            }).findFirst().orElseThrow(() -> new RuntimeException("获取bean或者方法异常"));
            operation.setAccessible(true);
            result = operation.invoke(client, params);
            log.info("call({}, {}, {}) = {}", clientName, operationName, Arrays.toString(params), result);
        } catch (Exception e) {
            log.error("exception when call({}, {}, {})", clientName, operationName, Arrays.toString(params), e);
        }
        return result;
    }
}