package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RiskRequestDTO {

    private String strategyCode;

    private Map<String,Object> data;

}
