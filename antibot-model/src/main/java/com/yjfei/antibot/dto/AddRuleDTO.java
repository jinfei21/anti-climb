package com.yjfei.antibot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddRuleDTO {

    private Long id;

    @ApiModelProperty("策略ID")
    @NotNull(message = "策略ID不能为空")
    private Long strategyId;

    private String name;

    private Integer priority;

    private Integer score;

    @ApiModelProperty("规则条件")
    private List<RuleConditionDTO> conditions;
}
