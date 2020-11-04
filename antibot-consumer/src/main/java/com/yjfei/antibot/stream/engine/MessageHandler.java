package com.yjfei.antibot.stream.engine;

import com.yjfei.antibot.data.Message;

/**
 * 消息处理器
 * 调用方需小心处理所有异常信息
 */
public interface MessageHandler {
    /**
     * 处理消息
     *
     * @param message 消息数据
     * @return 处理结果
     */
    boolean handle(Message message);
}