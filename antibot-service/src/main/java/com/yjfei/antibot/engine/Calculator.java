package com.yjfei.antibot.engine;

/**
 * 可计算的类
 */
public interface Calculator<R> {

    /**
     * 风控计算
     *
     * @param context
     * @return
     */
    R calculate(Context context);
}
