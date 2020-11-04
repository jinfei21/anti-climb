package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TestVariableDTO {

    private String strategyCode;

    private Long variableId;

    private Long timestamp;

    private Map<String ,Object> data;
}
