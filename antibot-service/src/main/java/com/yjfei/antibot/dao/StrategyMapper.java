package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.StrategyBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface StrategyMapper extends Mapper<StrategyBean> {

    StrategyBean selectByCode(@Param("code") String code);
}
