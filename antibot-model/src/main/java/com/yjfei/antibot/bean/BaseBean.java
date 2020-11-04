package com.yjfei.antibot.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 基础bean，提供基础信息
 */
@Data
@MappedSuperclass
public abstract class BaseBean {
    /**
     * 唯一ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 最后更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 最后更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
