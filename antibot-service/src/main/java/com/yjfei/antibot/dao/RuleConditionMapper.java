package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.RuleConditionBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RuleConditionMapper extends Mapper<RuleConditionBean> {


    /**
     * 根据策略id查询规则条件列表
     *
     * @param strategyId 策略ID
     * @return 对应规则条件列表
     */
    List<RuleConditionBean> selectConditionByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据变量id查询规则条件列表
     *
     * @param variableId
     * @return 对应规则条件列表
     */
    List<RuleConditionBean> selectConditionByVariableId(@Param("variableId") Long variableId);


    /**
     * 根据规则ID查询规则条件列表
     *
     * @param ruleId 规则ID
     * @return 对应的规则条件列表
     */
    List<RuleConditionBean> selectConditionByRuleId(@Param("ruleId") Long ruleId);

    /**
     * 根据规则ID删除规则条件列表
     *
     * @param ids
     * @return
     */
    int deleteConditionByIds(@Param("ids") List<Long> ids);

}
