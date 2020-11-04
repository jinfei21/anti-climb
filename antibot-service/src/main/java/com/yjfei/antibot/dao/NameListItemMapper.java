package com.yjfei.antibot.dao;

import com.yjfei.antibot.bean.NameListItemBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


import java.util.Date;
import java.util.List;

@Repository
public interface NameListItemMapper extends Mapper<NameListItemBean> {

    /**
     * 根据名单ID查询名单项
     *
     * @param nameListId 名单ID
     * @return 名单项列表
     */
    List<NameListItemBean> findByNameListId(@Param("nameListId") long nameListId);

    /**
     * 查询指定key对应的名单项
     *
     * @param nameListId 名单ID
     * @param key        指定key
     * @return 对应的名单项
     */
    NameListItemBean findNameListItem(@Param("nameListId") long nameListId, @Param("key") String key);


    /**
     * 删除名单
     *
     * @param nameListId
     */
    void deleteByNameListId(@Param("nameListId") long nameListId);

    /**
     * 查询名单列表
     *
     * @param key
     * @param type
     * @param effectDate
     * @param expireDate
     * @return
     */
    List<NameListItemBean> queryNameListItem(@Param("key") String key, @Param("type") Integer type, @Param("effectDate") Date effectDate, @Param("expireDate") Date expireDate);
}
