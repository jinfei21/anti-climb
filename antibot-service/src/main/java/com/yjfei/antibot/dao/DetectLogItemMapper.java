package com.yjfei.antibot.dao;


import com.yjfei.antibot.bean.DetectLogItemBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface DetectLogItemMapper extends Mapper<DetectLogItemBean>{

    List<DetectLogItemBean> queryDetectLogItem(@Param("strategyId") Long strategyId, @Param("ruleId") Long ruleId, @Param("effectDate") Date effectDate, @Param("expireDate") Date expireDate);

}
