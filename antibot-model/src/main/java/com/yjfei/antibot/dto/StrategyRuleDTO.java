package com.yjfei.antibot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StrategyRuleDTO {

    private Long id;

    @ApiModelProperty("策略ID")
    private Long strategyId;

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("规则逻辑")
    private String logicString;

    @ApiModelProperty("规则条件")
    private List<RuleConditionDTO> conditionList;

    @ApiModelProperty("规则状态")
    private Integer status;

    @ApiModelProperty("规则分值")
    private Integer score;

    @ApiModelProperty("优先级")
    private Integer priority;

}
