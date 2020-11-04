package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyDTO {

    private Long id;

    private String name;

    private String code;

    private Long modelId;

    private Integer high;

    private Integer mid;

    private String owner;

    private String depart;

    private String email;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
