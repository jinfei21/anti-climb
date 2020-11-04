package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_stream_data_sources`")
public class DataSourceBean extends BaseBean{
    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;


    /**
     * 数据源类型
     * 0：ONS
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 地址
     */
    @Column(name = "`address`")
    private String address;

    /**
     * 连接用户名
     */
    @Column(name = "`username`")
    private String username;

    /**
     * 连接密码
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 分组，如数据库的DB name，MQ的topic
     */
    @Column(name = "`group`")
    private String group;

    /**
     * 自分组，如数据库的表，MQ的tag（如果有）
     */
    @Column(name = "`sub_group`")
    private String subGroup;



}
