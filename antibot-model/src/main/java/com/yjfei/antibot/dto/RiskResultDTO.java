package com.yjfei.antibot.dto;

import com.yjfei.antibot.common.RiskType;
import lombok.Data;

@Data
public class RiskResultDTO {

    private RiskType riskType;

    private int score;

    private String action;
}
