package com.yjfei.antibot.dto;


import lombok.Data;

@Data
public class AddDatasourceDTO {
    private Long id;

    private String name;

    private String type;

    private String address;

    private String username;

    private String password;

    private String group;

    private String subGroup;
}
