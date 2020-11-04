package com.yjfei.antibot.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = AddVariableDTO.OTFVariableDTO.class, name = "0"),
        @JsonSubTypes.Type(value = AddVariableDTO.NamelistVariableDTO.class, name = "1"),
        @JsonSubTypes.Type(value = AddVariableDTO.RADDVariableDTO.class, name = "2"),
        @JsonSubTypes.Type(value = AddVariableDTO.TumbleVariableDTO.class, name = "3") })
public abstract class AddVariableDTO {

    private Long id;

    @ApiModelProperty("变量名称")
    private String name;

    @ApiModelProperty("变量类型")
    private Integer type;

    @ApiModelProperty("变量默认值")
    private String defValue;

    @ApiModel("OTF变量创建请求")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class OTFVariableDTO extends AddVariableDTO{
        @ApiModelProperty("计算逻辑表达式")
        private String logicExpression;
    }

    @ApiModel("名单变量创建请求")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class NamelistVariableDTO extends AddVariableDTO{

        @ApiModelProperty("名单ID")
        private Long nameListId;

        @ApiModelProperty("KEY计算逻辑表达式")
        private String keyExpression;
    }

    @ApiModel("RADD变量创建请求")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class RADDVariableDTO extends AddVariableDTO{
        @ApiModelProperty("streaming RADD ID")
        private Long streamingVariableId;

        @ApiModelProperty("KEY计算逻辑表达式")
        private String keyExpression;
    }

    @ApiModel("滚动窗口变量创建请求")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class TumbleVariableDTO extends AddVariableDTO{
        @ApiModelProperty("streaming rollup变量ID")
        private Long streamingVariableId;

        @ApiModelProperty("KEY计算逻辑表达式")
        private String keyExpression;

        @ApiModelProperty("ROLLUP变量的时间长度，分钟不能超过60，小时不能超过24，天不能超过一个月")
        private Integer period;

        @ApiModelProperty("ROLLUP变量的时间单位：m 分钟，h 小时，d 天")
        private String timeUnit;
    }
}
