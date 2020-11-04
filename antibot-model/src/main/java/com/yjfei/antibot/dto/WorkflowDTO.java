package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WorkflowDTO {

    private Long id;

    private String name;

    private String type;

    private Long dataSourceId;

    private String pkPattern;

    private String etlPattern;

    private String tsPattern;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
