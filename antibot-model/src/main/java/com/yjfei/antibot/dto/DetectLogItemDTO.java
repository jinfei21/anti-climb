package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetectLogItemDTO {

    private Long id;

    private Long strategyId;

    private Long ruleId;

    private Integer score;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
