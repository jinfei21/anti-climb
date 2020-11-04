package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.RuleBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


import java.util.List;

@Repository
public interface RuleMapper extends Mapper<RuleBean> {

    /**
     * 根据策略ID查询规则列表
     *
     * @param  strategyId 策略ID
     * @return 对应的规则列表
     */
    List<RuleBean> selectByStrategyId(@Param("strategyId") Long strategyId);


    int updateStatusByIds(@Param("ids") List<Long> ids,@Param("state") Integer state);


    List<RuleBean> selectByRuleNameAndStrategyId(@Param("strategyId") Long strategyId,@Param("name") String name);
}
