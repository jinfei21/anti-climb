package com.yjfei.antibot.service;

import com.yjfei.antibot.bean.StrategyBean;
import com.yjfei.antibot.dao.StrategyMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class StrategyService {


    @Autowired
    private StrategyMapper strategyMapper;


    public StrategyBean getStrategyByCode(String code){
        return strategyMapper.selectByCode(code);
    }


    public void addStrategy(StrategyBean strategyBean){
        if (strategyBean.getId() == null){
            strategyBean.setCreateBy("admin");
            strategyBean.setUpdateBy(strategyBean.getCreateBy());
            strategyMapper.insert(strategyBean);
        }else {
            strategyBean.setUpdateBy("admin");
            strategyMapper.updateByPrimaryKeySelective(strategyBean);
        }
    }

    @Transactional
    public void deleteStrategyById(Long id){
        strategyMapper.deleteByPrimaryKey(id);
    }

    public StrategyBean getStrategyById(Long id){
        return strategyMapper.selectByPrimaryKey(id);
    }

    public Page<StrategyBean> list(Integer page, Integer pageSize){

        Page<StrategyBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> strategyMapper.selectAll());

        return pageResult;

    }

}
