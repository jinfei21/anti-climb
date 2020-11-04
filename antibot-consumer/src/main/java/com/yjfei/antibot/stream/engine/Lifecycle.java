package com.yjfei.antibot.stream.engine;

/**
 * 标识类带有生命周期管理
 */
public interface Lifecycle {
    /**
     * 开始运行
     */
    void start();

    /**
     * 停止运行
     */
    void stop();

    /**
     * 判断当前组件是否正在运行
     *
     * @return 是否正在运行
     */
    boolean isRunning();
}
