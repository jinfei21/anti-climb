package com.yjfei.antibot.engine.model;

/**
 * 标识可以做数据转换的类
 *
 * @param <T> 转换结果数据的类型
 */
public interface Castable<T> {

    /**
     * 转换数据
     *
     * @param original 原始数据
     * @return 转换后的数据
     */
    T cast(Object original);
}
