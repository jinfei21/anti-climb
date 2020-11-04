package com.yjfei.antibot.service;

import com.yjfei.antibot.bean.NameListBean;
import com.yjfei.antibot.bean.NameListItemBean;
import com.yjfei.antibot.dao.NameListItemMapper;
import com.yjfei.antibot.dao.NameListMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class NameListService {

    @Autowired
    private NameListItemMapper nameListItemMapper;

    @Autowired
    private NameListMapper nameListMapper;

    public NameListItemBean findNameListItem(long nameListId, String key) {
        return nameListItemMapper.findNameListItem(nameListId,key);
    }

    public void addNameList(NameListBean nameListBean){
        if (nameListBean.getId() == null){
            nameListBean.setCreateBy("admin");
            nameListBean.setUpdateBy(nameListBean.getCreateBy());
            nameListMapper.insert(nameListBean);
        }else {
            nameListBean.setUpdateBy("admin");
            nameListMapper.updateByPrimaryKeySelective(nameListBean);
        }
    }

    @Transactional
    public void deleteNameList(Long id){
        nameListMapper.deleteByPrimaryKey(id);
        nameListItemMapper.deleteByNameListId(id);
    }

    @Transactional
    public void deleteNameListItem(Long id){
        nameListItemMapper.deleteByPrimaryKey(id);
    }

    public void addNameListItem(NameListItemBean nameListItemBean){
        if (nameListItemBean.getId() == null){
            nameListItemBean.setCreateBy("admin");
            nameListItemBean.setUpdateBy(nameListItemBean.getCreateBy());
            nameListItemMapper.insert(nameListItemBean);
        }else {
            nameListItemBean.setUpdateBy("admin");
            nameListItemMapper.updateByPrimaryKeySelective(nameListItemBean);
        }
    }

    public Page<NameListBean> listNameList(Integer page, Integer pageSize){
        Page<NameListBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> nameListMapper.selectAll());

        return pageResult;
    }

    public Page<NameListItemBean> listNameListItem(String key, Integer type, Date effectDate, Date expireDate, Integer page, Integer pageSize){

        Page<NameListItemBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> nameListItemMapper.queryNameListItem(key,type,effectDate,expireDate));

        return pageResult;
    }



}
