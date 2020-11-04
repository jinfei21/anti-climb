package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_stream_variables`")
public class StreamVariableBean extends BaseBean {

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 工作流ID
     */
    @Column(name = "`workflow_id`")
    private Long workflowId;

    /**
     * 变量类型
     * 0：ROLLUP
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 子类型
     */
    @Column(name = "`sub_type`")
    private Integer subType;

    /**
     * key，如表达key位置的表达式
     */
    @Column(name = "`key_pattern`")
    private String keyPattern;

    /**
     * 过滤逻辑（如果有），如条件
     */
    @Column(name = "`filter_pattern`")
    private String filterPattern;

    /**
     * 算子
     */
    @Column(name = "`agg_pattern`")
    private String aggPattern;


    /**
     * 数据类型
     */
    @Column(name = "`value_type`")
    private Integer valueType;

}
