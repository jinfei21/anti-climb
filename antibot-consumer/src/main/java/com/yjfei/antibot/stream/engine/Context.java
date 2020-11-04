package com.yjfei.antibot.stream.engine;

import com.yjfei.antibot.data.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Context extends HashMap<String, Object> {
    private final transient Map<Long, VariableValue> variableMap;
    private final transient Map<Expression, Object> expressionMap;
    private long timestamp;

    public Context() {
        super();
        this.variableMap = new HashMap<>();
        this.expressionMap = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
    }

    public Context(Message message) {
        super(message);
        this.variableMap = new HashMap<>();
        this.expressionMap = new HashMap<>();
        this.timestamp = message.getTimestamp();
    }

    /**
     * 给定的变量ID是否已经计算过值
     *
     * @param variableId 变量ID
     * @return 对应的变量值
     */
    public boolean containsVariableValue(long variableId) {
        return variableMap.containsKey(variableId);
    }

    /**
     * 获取变量的值
     *
     * @param variableId 变量ID
     * @return 变量值
     */
    public VariableValue getVariableValue(long variableId) {
        return variableMap.get(variableId);
    }

    /**
     * 添加变量值
     *
     * @param variableId    变量ID
     * @param variableValue 变量值
     */
    public void putVariableValue(long variableId, VariableValue variableValue) {
        variableMap.put(variableId, variableValue);
    }

    /**
     * 给定的表达式是否已经计算过值
     *
     * @param expression 表达式
     * @return 对应的表达式值
     */
    public boolean containsExpression(Expression expression) {
        return expressionMap.containsKey(expression);
    }

    /**
     * 获取表达式的值
     *
     * @param expression 表达式
     * @return 表达式值
     */
    public Object getExpression(Expression expression) {
        return expressionMap.get(expression);
    }


    /**
     * 添加表达式值
     *
     * @param expression 表达式
     * @param value      表达式值
     */
    public void putExpression(Expression expression, Object value) {
        expressionMap.put(expression, value);
    }

    public long getTimestamp() {
        return timestamp;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Context context = (Context) o;
        return timestamp == context.timestamp &&
                Objects.equals(variableMap, context.variableMap) &&
                Objects.equals(expressionMap, context.expressionMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), variableMap, expressionMap, timestamp);
    }

    @Override
    public String toString() {
        return "Context{" +
                "variableMap=" + variableMap +
                ", expressionMap=" + expressionMap +
                ", other=" + super.toString() +
                '}';
    }
}
