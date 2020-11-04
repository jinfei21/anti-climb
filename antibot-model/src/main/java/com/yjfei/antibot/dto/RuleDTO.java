package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RuleDTO {

    private Long id;

    private Long strategyId;

    private String name;

    private Integer priority;

    private String logicString;

    private Integer status;

    private Integer score;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
