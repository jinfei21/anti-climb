package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_rule`")
public class RuleBean extends BaseBean{

    /**
     * 策略ID
     */
    @Column(name = "strategy_id")
    private Long strategyId;


    /**
     * 规则名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 得分
     */
    @Column(name = "score")
    private Integer score;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 规则状态
     */
    @Column(name = "status")
    private Integer status;


    public boolean isValid() {
        return 1 == status;
    }

}
