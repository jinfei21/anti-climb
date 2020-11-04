package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TestRuleDTO {

    private String strategyCode;

    private Long ruleId;

    private Map<String ,Object> data;
}
