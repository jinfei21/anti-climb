package com.yjfei.antibot.dto;

import lombok.Data;

@Data
public class AddStrategyDTO {

    private Long id;

    private String name;

    private String code;

    private Long modelId;

    private Integer high;

    private Integer mid;

    private String owner;

    private String depart;

    private String email;

}
