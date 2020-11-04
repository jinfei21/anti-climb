package com.yjfei.antibot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RuleConditionDTO {

    private Long id;

    @ApiModelProperty("变量ID")
    private Long variableId;

    @ApiModelProperty("比较操作的编码")
    private String operation;

    @ApiModelProperty("比较操作的因子")
    private String factor;

    @ApiModelProperty("与下一个条件之间的关系")
    private String relation;
}
