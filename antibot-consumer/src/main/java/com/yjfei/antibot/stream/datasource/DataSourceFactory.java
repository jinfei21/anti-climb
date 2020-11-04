package com.yjfei.antibot.stream.datasource;

import com.yjfei.antibot.bean.DataSourceBean;
import com.yjfei.antibot.common.DataSourceType;
import com.yjfei.antibot.stream.engine.DataSource;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("all")
public class DataSourceFactory {

    private static DataSourceFactory instance;

    private Map<Integer,Class<? extends DataSource>> dataSourceMap = new HashMap<>();

    private DataSourceFactory(){
        dataSourceMap.put(DataSourceType.ONS.getCode(),ONSDataSource.class);
    }

    public static DataSourceFactory getInstance(){
        if (instance == null){
            synchronized (DataSourceFactory.class){
                if (instance == null){
                    instance = new DataSourceFactory();
                }
            }
        }
        return instance;
    }

    public DataSource getDataSource(ApplicationContext applicationContext, DataSourceBean dataSourceBean){

        Class<? extends DataSource> dataSourceClass = this.dataSourceMap.get(dataSourceBean.getType());

        try{
            Constructor<? extends DataSource> constructor = dataSourceClass.getConstructor(ApplicationContext.class,DataSourceBean.class);

            return constructor.newInstance(applicationContext,dataSourceBean);
        }catch (Exception e){
            throw new IllegalArgumentException("datasource type error："+dataSourceBean.getType());
        }

    }
}
