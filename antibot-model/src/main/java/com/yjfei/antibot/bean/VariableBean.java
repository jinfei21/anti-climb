package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_variable`")
public class VariableBean extends BaseBean{

    /**
     * 变量名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 变量类型
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 源ID
     * 当变量是RADD、ROLLUP时指对应的streaming variable id
     * 当变量是名单时指名单ID
     */
    @Column(name = "source_id")
    private Long sourceId;


    /**
     * OTF的计算逻辑表达式
     */
    @Column(name = "logic_expr")
    private String logicExpression;

    /**
     * 查询KEY的计算逻辑表达式
     */
    @Column(name = "key_expr")
    private String keyExpression;

    /**
     * ROLLUP变量的时间长度
     */
    @Column(name = "period")
    private Integer period;

    /**
     * ROLLUP变量的时间单位：h 小时，d 天
     */
    @Column(name = "time_unit")
    private String timeUnit;

    /**
     * 变量默认值
     */
    @Column(name = "def_value")
    private String defValue;




}
