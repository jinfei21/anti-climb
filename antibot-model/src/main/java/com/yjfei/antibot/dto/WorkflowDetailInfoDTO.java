package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WorkflowDetailInfoDTO {

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

    private List<StreamVariableDTO> variables;
}
