package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.bean.DataSourceBean;
import com.yjfei.antibot.stream.dao.DataSourceMapper;
import com.yjfei.antibot.stream.datasource.DataSourceFactory;
import com.yjfei.antibot.stream.engine.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DataSourceService {

    private final DataSourceMapper dataSourceMapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public DataSourceService(DataSourceMapper dataSourceMapper, ApplicationContext applicationContext){
        this.dataSourceMapper = dataSourceMapper;
        this.applicationContext = applicationContext;
    }

    public DataSource getDataSource(Long dataSourceId){
        DataSourceBean dataSourceBean = dataSourceMapper.findById(dataSourceId);

        if (dataSourceBean == null) throw new RuntimeException("datasource is not existed,id :" + dataSourceId);
        return DataSourceFactory.getInstance().getDataSource(applicationContext,dataSourceBean);
    }

}
