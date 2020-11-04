package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.VariableBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface VariableMapper extends Mapper<VariableBean> {

    /**
     * 根据ID查询变量
     *
     * @param id 变量ID
     * @return 变量
     */
    VariableBean selectById(@Param("id") Long id);

}
