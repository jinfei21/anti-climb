package com.yjfei.antibot.stream.dao;

import com.yjfei.antibot.bean.WorkflowBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.base.BaseSelectMapper;

import java.util.List;

@Repository
public interface WorkflowMapper extends BaseSelectMapper<WorkflowBean> {


    /**
     * 根据类型查询工作流
     *
     * @param type 类型
     * @return 工作流列表
     */
    List<WorkflowBean> selectByType(@Param("type") Integer type);
}
