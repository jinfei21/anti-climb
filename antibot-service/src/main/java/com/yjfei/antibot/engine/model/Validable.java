package com.yjfei.antibot.engine.model;

/**
 * 标识可以做数据校验的类
 *
 * @param <T> 待校验数据的类型
 */
public interface Validable<T> {

    /**
     * 校验数据是否合法
     *
     * @param data 待校验的数据
     */
    void validate(T data);
}
