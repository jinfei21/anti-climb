package com.yjfei.antibot.stream.engine;

import com.yjfei.antibot.bean.StreamVariableBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 变量的值
 */
@AllArgsConstructor
@Getter
@ToString
public class VariableValue {
    public static final VariableValue NULL = new VariableValue();

    /**
     * 变量的ID
     */
    private StreamVariableBean bean;

    /**
     * 变量的key
     */
    private String key;

    /**
     * 变量的值
     */
    private Object value;

    /**
     * 时间戳
     */
    private long timestamp;

    private VariableValue() {
    }

    public boolean isNotEmpty() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
