package com.yjfei.antibot.stream.dao;

import com.yjfei.antibot.bean.DataSourceBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface DataSourceMapper extends BaseMapper<DataSourceBean> {

    DataSourceBean findById(@Param("id") Long id);
}
