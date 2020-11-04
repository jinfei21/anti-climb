package com.yjfei.antibot.service;

import com.yjfei.antibot.bean.DataSourceBean;
import com.yjfei.antibot.dao.DataSourceMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;


    public void addDataSource(DataSourceBean dataSourceBean){

        if (dataSourceBean.getId() == null){
            dataSourceBean.setCreateBy("admin");
            dataSourceBean.setUpdateBy(dataSourceBean.getCreateBy());
            dataSourceMapper.insert(dataSourceBean);
        }else {
            dataSourceBean.setUpdateBy("admin");
            dataSourceMapper.updateByPrimaryKey(dataSourceBean);
        }
    }

    public void deleteDataSourceById(Long id){
        dataSourceMapper.deleteByPrimaryKey(id);
    }

    public DataSourceBean getDataSourceById(Long id){
        return dataSourceMapper.selectByPrimaryKey(id);
    }

    public Page<DataSourceBean> list(Integer page, Integer pageSize){

        Page<DataSourceBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> dataSourceMapper.selectAll());


        return pageResult;
    }
}
