package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NameListItemDTO {
    private Long id;

    private Long nameListId;

    private String key;

    private String reason;

    private String type;

    private Date effectDate;

    private Date expireDate;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
