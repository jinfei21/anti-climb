package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.ModelFieldBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ModelFieldMapper extends Mapper<ModelFieldBean> {

    /**
     * 根据模型ID查询数据模型字段列表
     *
     * @param modelId 模型ID
     * @return 数据模型字段列表
     */
    List<ModelFieldBean> selectByModelId(@Param("modelId") Long modelId);

    void deleteByModelId(@Param("modelId") Long modelId);
}
