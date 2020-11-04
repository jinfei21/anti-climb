package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.dao.VariableMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariableService {


    @Autowired
    private VariableMapper variableMapper;


    public void addVariable(VariableBean variableBean){
        if (variableBean.getId() == null){
            variableBean.setCreateBy("admin");
            variableBean.setUpdateBy(variableBean.getCreateBy());
            variableMapper.insert(variableBean);
        }else {
            variableBean.setUpdateBy("admin");
            variableMapper.updateByPrimaryKeySelective(variableBean);
        }
    }

    public Page<VariableBean> list(Integer page, Integer pageSize){

        Page<VariableBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> variableMapper.selectAll());

        return pageResult;

    }


    }
