package com.yjfei.antibot.stream.dao;

import com.yjfei.antibot.bean.StreamVariableBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import java.util.List;

@Repository
public interface VariableMapper extends BaseSelectMapper<StreamVariableBean> {

    /**
     * 根据工作流ID查询变量列表
     *
     * @param workflowId 工作流ID
     * @return 变量列表
     */
    List<StreamVariableBean> selectByWorkflowIdAndType(@Param("workflowId") long workflowId, @Param("type") int type);
}


