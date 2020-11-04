package com.yjfei.antibot.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "`antibot_namelist`")
public class NameListBean extends BaseBean{

    /**
     * 名单名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;
}
