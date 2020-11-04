package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_stream_workflows`")
public class WorkflowBean extends BaseBean {

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 类型
     * 0：ROLLUP
     * 1：RADD
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 数据源ID
     */
    @Column(name = "`ds_id`")
    private Long dataSourceId;


    /**
     * 主键表达式
     */
    @Column(name = "`pk_pattern`")
    private String pkPattern;

    /**
     * ETL表达式
     */
    @Column(name = "`etl_pattern`")
    private String etlPattern;

    /**
     * 时间戳表达式
     */
    @Column(name = "`ts_pattern`")
    private String tsPattern;
}
