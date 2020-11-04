package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.DetectLogBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface DetectLogMapper extends Mapper<DetectLogBean>{

    List<DetectLogBean> queryDetectLog(@Param("strategyId") Long strategyId,@Param("level") Integer level, @Param("effectDate")  Date effectDate, @Param("expireDate") Date expireDate);
}
