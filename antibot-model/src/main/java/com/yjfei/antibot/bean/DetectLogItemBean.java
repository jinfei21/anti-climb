package com.yjfei.antibot.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_detect_log_item`")
public class DetectLogItemBean extends BaseBean{

    /**
     * 策略ID
     */
    @Column(name = "`strategy_id`")
    private Long strategyId;

    /**
     * 策略ID
     */
    @Column(name = "`rule_id`")
    private Long ruleId;

    /**
     * 分值
     */
    @Column(name = "`score`")
    private Integer score;

}
