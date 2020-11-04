package com.yjfei.antibot.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_model_field`")
public class ModelFieldBean extends BaseBean{

    /**
     * 归属的数据模型ID
     */
    @Column(name = "model_id")
    private Long modelId;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 参数数据类型
     */
    @Column(name = "data_type")
    private Integer dataType;

    /**
     * 是否必填，0否，1是，默认值为0
     */
    @Column(name = "required")
    private Boolean required;

    /**
     * 默认值
     */
    @Column(name = "`def_value`")
    private String defValue;

}
