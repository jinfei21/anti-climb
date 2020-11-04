package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ModelFieldDTO {
    private Long id;

    private String code;

    private String name;

    private String dataType;

    private Boolean required;

    private String defValue;

}
