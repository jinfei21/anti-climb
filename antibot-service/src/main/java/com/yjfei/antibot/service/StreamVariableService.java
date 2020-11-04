package com.yjfei.antibot.service;

import com.yjfei.antibot.bean.StreamVariableBean;
import com.yjfei.antibot.dao.StreamVariableMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamVariableService {

    @Autowired
    private StreamVariableMapper streamVariableMapper;

    public StreamVariableBean getVariableById(long id){
        StreamVariableBean streamVariableBean = streamVariableMapper.selectByPrimaryKey(id);
        return streamVariableBean;
    }


    public void addStreamVariable(StreamVariableBean streamVariableBean){

        if (streamVariableBean.getId() == null){
            streamVariableBean.setCreateBy("admin");
            streamVariableBean.setUpdateBy(streamVariableBean.getCreateBy());
            streamVariableMapper.insert(streamVariableBean);
        }else{
            streamVariableBean.setUpdateBy("admin");
            streamVariableMapper.updateByPrimaryKeySelective(streamVariableBean);
        }
    }

    public void deleteStreamVariableById(Long id){
         streamVariableMapper.deleteByPrimaryKey(id);
    }

    public Page<StreamVariableBean> list(Long workflowId,Integer page, Integer pageSize){

        Page<StreamVariableBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> {
                            if (workflowId != null) {
                                streamVariableMapper.selectByWorkflowId(workflowId);
                            } else {
                                streamVariableMapper.selectAll();
                            }
                        }
                );


        return pageResult;

    }

}
