package com.yjfei.antibot.engine.rule;


import com.yjfei.antibot.engine.Context;

/**
 * 规则条件
 */
@FunctionalInterface
public interface RuleCondition {

    /**
     * 计算
     *
     * @param context 计算上下文
     * @return 计算结果
     */
    boolean apply(Context context);

    static RuleCondition named(String name, RuleCondition condition) {

        return new RuleCondition() {

            @Override
            public boolean apply(Context context) {
                return condition.apply(context);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    /**
     * 两个条件异或操作
     *
     * @param other 另一个条件
     * @return 两个条件异或操作的条件
     */
    default RuleCondition xor(RuleCondition other) {
        return named("(" + this.toString() + ")\nX(" + other + ")", context -> apply(context) ^ other.apply(context));
    }

    /**
     * 两个条件或操作
     *
     * @param other 另一个条件
     * @return 两个条件或操作的条件
     */
    default RuleCondition or(RuleCondition other){
        return named("(" + this.toString() + ")\n||(" + other + ")", context -> apply(context) || other.apply(context));
    }

    /**
     * 两个条件与操作
     *
     * @param other 另一个条件
     * @return 两个条件与操作的条件
     */
    default RuleCondition and(RuleCondition other){
        return named("(" + this.toString() + ")\n&&(" + other + ")", context -> apply(context) && other.apply(context));
    }
}
