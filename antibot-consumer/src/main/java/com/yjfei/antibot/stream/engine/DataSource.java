package com.yjfei.antibot.stream.engine;

/**
 * 标识一种数据源，如MQ、DB等
 */
public interface DataSource extends Lifecycle{
    /**
     * 连接数据源
     *
     * @param messageHandler 消息处理器
     */
    void connectWith(MessageHandler messageHandler);
}