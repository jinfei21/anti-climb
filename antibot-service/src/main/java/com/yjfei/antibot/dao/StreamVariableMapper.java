package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.StreamVariableBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 变量查询相关
 */
@Repository
public interface StreamVariableMapper extends Mapper<StreamVariableBean> {

    /**
     * 根据工作流ID查询变量列表
     *
     * @param workflowId 工作流ID
     * @return 变量列表
     */
    List<StreamVariableBean> selectByWorkflowId(@Param("workflowId") Long  workflowId);


    void deleteByWorkflowId(@Param("workflowId") Long workflowId);

}
