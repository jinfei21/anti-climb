package com.yjfei.antibot.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_detect_log`")
public class DetectLogBean extends BaseBean{

    /**
     * 策略ID
     */
    @Column(name = "`strategy_id`")
    private Long strategyId;

    /**
     * 策略名称
     */
    @Column(name = "`strategy_code`")
    private String strategyCode;

    /**
     * 主机ip
     */
    @Column(name = "`host`")
    private String host;

    /**
     * 风险等级
     */
    @Column(name = "`risk_level`")
    private Integer riskLevel;

    /**
     * 分值
     */
    @Column(name = "`score`")
    private Integer score;

    /**
     * 耗时
     */
    @Column(name = "`cost_time`")
    private Long cost;

}
