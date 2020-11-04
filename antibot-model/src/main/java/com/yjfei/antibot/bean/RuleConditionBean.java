package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_rule_condition`")
public class RuleConditionBean extends BaseBean{

    /**
     * 规则ID
     */
    @Column(name = "rule_id")
    private Long ruleId;

    /**
     * 变量ID
     */
    @Column(name = "variable_id")
    private Long variableId;

    /**
     * 比较操作的编码
     */
    @Column(name = "operation")
    private String operation;

    /**
     * 比较操作的因子
     */
    @Column(name = "factor")
    private String factor;

    /**
     * 上一个条件的ID
     */
    @Column(name = "pre_id")
    private Long preConditionId;

    /**
     * 与上一个条件之间的关系
     */
    @Column(name = "relation")
    private String relation;
}
