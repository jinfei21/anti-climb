package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.engine.Calculator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;

import static com.yjfei.antibot.engine.rule.RuleCondition.named;

/**
 * 规则条件中的变量
 */
@FunctionalInterface
public interface RuleVariable<R> extends Calculator<R> {


    /**
     * 生成判断结果是否等于给定factor的规则条件
     *
     * @param factor 给定因子
     * @return 判断结果是否等于给定factor的规则条件
     */
    default RuleCondition same(String factor) {
        return named(this.toString() + "==" + factor,
                context -> {
                    Object result = calculate(context);
                    if (factor == null || result == null) {
                        return result == factor;
                    }
                    if (Number.class.isAssignableFrom(result.getClass())) {
                        double factorNumber = NumberUtils.toDouble(factor);
                        double resultNumber = ((Number) result).doubleValue();

                        return Math.abs(resultNumber - factorNumber) <= 0.000001D;
                    } else {
                        return factor.equals(result);
                    }
                });
    }

    /**
     * 生成判断结果是否不等于给定factor的规则条件
     *
     * @param factor 给定因子
     * @return 判断结果是否不等于给定factor的规则条件
     */
    default RuleCondition notSame(String factor) {
        return named(this.toString() + "!=" + factor,
                context -> {
                    Object result = calculate(context);
                    if (factor == null || result == null) {
                        return result != factor;
                    }

                    if (Number.class.isAssignableFrom(result.getClass())) {
                        double factorNumber = NumberUtils.toDouble(factor);
                        double resultNumber = ((Number) result).doubleValue();

                        return Math.abs(resultNumber - factorNumber) > 0.000001D;
                    } else {
                        return !factor.equals(result);
                    }
                });
    }

    /**
     * 生成判断结果是否大于给定factor的规则条件
     *
     * @param factor 给定因子
     * @return 判断结果是否大于给定factor的规则条件
     */
    default RuleCondition greaterThan(String factor) {
        if (NumberUtils.isCreatable(factor)) {
            double factorNumber = NumberUtils.toDouble(factor);
            return named(this.toString() + ">" + factor, context -> {
                Number result = (Number) calculate(context);
                return result != null && result.doubleValue() > factorNumber;
            });
        } else {
            throw new IllegalArgumentException("factor is not a number type:" + factor);
        }
    }

    /**
     * 生成判断结果是否大于等于给定factor的规则条件
     *
     * @param factor 给定因子
     * @return 判断结果是否大于等于给定factor的规则条件
     */
    default RuleCondition greaterThanEqual(String factor) {
        if (NumberUtils.isCreatable(factor)) {
            double factorNumber = NumberUtils.toDouble(factor);

            return named(this.toString() + ">=" + factor, context -> {
                Number result = (Number) calculate(context);
                return result != null && (result.doubleValue() > factorNumber || Math.abs(result.doubleValue() - factorNumber) <= 0.000001D);
            });
        } else {
            throw new IllegalArgumentException("factor is not a number type:" + factor);
        }
    }

    /**
     * 生成判断结果是否小于给定factor的规则条件
     *
     * @param factor 给定因子
     * @return 判断结果是否小于给定factor的规则条件
     */
    default RuleCondition lessThan(String factor){
        if (NumberUtils.isCreatable(factor)){
            double factorNumber = NumberUtils.toDouble(factor);

            return named(this.toString() +"<" + factor,context -> {
               Number result = (Number) calculate(context);
               return result != null && result.doubleValue() < factorNumber;
            });
        }else{
            throw new IllegalArgumentException("factor is not a number type:" + factor);
        }
    }

    /**
     * 生成判断结果是否小于等于给定factor的规则条件
     *
     * @param factor 给定因子
     * @return 判断结果是否小于等于给定factor的规则条件
     */
    default RuleCondition lessThanEqual(String factor){
        if (NumberUtils.isCreatable(factor)){
            double factorNumber = NumberUtils.toDouble(factor);

            return named(this.toString() +"<=" + factor,context -> {
                Number result = (Number)calculate(context);
                return result != null && (result.doubleValue() < factorNumber || Math.abs(result.doubleValue() - factorNumber)<=0.000001D);
            });
        }else{
            throw new IllegalArgumentException("factor is not a number type:" + factor);
        }
    }

    /**
     * 生成判断结果为true的规则条件
     *
     * @return 判断结果为true的规则条件
     */
    default RuleCondition isTrue(){
        return named(this.toString() + " == true",context -> (Boolean)calculate(context));
    }

    /**
     * 生成判断结果为false的规则条件
     *
     * @return 判断结果为false的规则条件
     */
    default RuleCondition isFalse(){
        return named(this.toString() + " == false",context -> !(Boolean)calculate(context));
    }

    /**
     * 生成判断结果是否在给定的factor中的规则条件
     *
     * @param factor 给定的factor列表，以,分割
     * @return 判断结果是否在给定的factor中的规则条件
     */
    default RuleCondition in(String factor){
        String[] factors = StringUtils.isNotEmpty(factor) ? factor.split(","):new String[0];

        return named(this.toString() +"in "+ Arrays.toString(factors),context -> {
            R val = calculate(context);
            String strVal = (val == null?null:val.toString());
            return Arrays.asList(factors).contains(strVal);
        });
    }

    /**
     * 生成判断结果是否不在给定的factor中的规则条件
     *
     * @param factor 给定的factor列表，以,分割
     * @return 判断结果是否不在给定的factor中的规则条件
     */
    default RuleCondition notIn(String factor){
        String[] factors = StringUtils.isNotEmpty(factor)?factor.split(","):new String[0];
        return named(this.toString() + "not in" + Arrays.toString(factors),context -> {
           R val = calculate(context);
           String strVal = (val == null?null:val.toString());
           return !Arrays.asList(factors).contains(strVal);
        });
    }
}
