package com.yjfei.antibot.dao;


import com.yjfei.antibot.bean.DataSourceBean;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface DataSourceMapper extends Mapper<DataSourceBean> {

}
