package com.yjfei.antibot.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的使用一个类似于JSON OBJECT的MAP来表达消息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Message extends HashMap<String, Object> {
    /**
     * 唯一ID
     */
    private String id;

    /**
     * 表
     */
    private String tableName;

    /**
     * 事件发生时间
     */
    private long timestamp;

    public Message(Map<String, Object> data) {
        super(data == null ? Collections.emptyMap() : data);
    }

    public Message(){
        super();
    }
}
