package com.yjfei.antibot.dto;

import lombok.Data;
import java.util.Date;


@Data
public class DataSourceDTO {
    private Long id;

    private String name;

    private String type;

    private String address;

    private String username;

    private String password;

    private String group;

    private String subGroup;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;
}
