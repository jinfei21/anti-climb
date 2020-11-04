package com.yjfei.antibot.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "antibot_namelist_item")
public class NameListItemBean extends BaseBean {

    /**
     * 归属名单ID
     */
    @Column(name = "namelist_id")
    private Long nameListId;

    /**
     * 查询KEY
     */
    @Column(name = "`key`")
    private String key;

    /**
     * 添加原因
     */
    @Column(name = "reason")
    private String reason;


    /**
     * 名单类型
     * 0白名单
     * 1灰名单
     * 2黑名单
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 生效时间
     */
    @Column(name = "effect_date")
    private Date effectDate;

    /**
     * 过期时间
     */
    @Column(name = "expire_date")
    private Date expireDate;

}
