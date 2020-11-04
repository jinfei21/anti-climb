package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetectLogDTO {

    private Long id;

    private Long strategyId;

    private String strategyCode;

    private String host;

    private Integer riskLevel;

    private Integer score;

    private Long cost;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
