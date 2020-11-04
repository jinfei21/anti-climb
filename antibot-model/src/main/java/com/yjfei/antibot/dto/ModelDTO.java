package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ModelDTO {
    private Long id;

    private String name;

    private String remark;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
