package com.yjfei.antibot.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_strategy`")
public class StrategyBean extends BaseBean {

    /**
     * 变量名称
     */
    @Column(name = "model_id")
    private Long modelId;

    /**
     * 策略code
     */
    @Column(name = "code")
    private String code;

    /**
     * 策略名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 高风险阀值
     */
    @Column(name = "high")
    private Integer high;

    /**
     * 中风险阀值
     */
    @Column(name = "mid")
    private Integer mid;

    /**
     * 负责人
     */
    @Column(name = "owner")
    private String owner;

    /**
     * 部门
     */
    @Column(name = "depart")
    private String depart;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
}
