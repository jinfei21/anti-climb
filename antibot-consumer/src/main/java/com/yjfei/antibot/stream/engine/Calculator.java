package com.yjfei.antibot.stream.engine;

/**
 * 算子
 *
 * @param <T> 结果类型
 */
public interface Calculator<T> {

    /**
     * 触发计算
     *
     * @param context 上下文
     * @return 结果
     */
    T calculate(Context context);
}
