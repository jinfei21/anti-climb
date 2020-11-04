package com.yjfei.antibot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VariableDTO {

    private Long id;

    private String name;

    private String defValue;

    private String timeUnit;

    private Integer period;

    private Integer type;

    private String keyExpression;

    private String logicExpression;

    private Long streamingVariableId;

    private Long nameListId;

    private List<String> factors;

    private String updateBy;

    private Date updateTime;

    private String createBy;

    private Date createTime;

}
