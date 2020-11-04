package com.yjfei.antibot.dao;


import com.yjfei.antibot.bean.ModelBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ModelMapper extends Mapper<ModelBean> {


    /**
     * 根据模型ID查询数据模型
     *
     * @param id 模型ID
     * @return 数据模型
     */
    ModelBean selectById(@Param("id") Long id);
}
