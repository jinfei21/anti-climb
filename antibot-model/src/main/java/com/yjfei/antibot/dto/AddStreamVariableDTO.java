package com.yjfei.antibot.dto;

import lombok.Data;

@Data
public class AddStreamVariableDTO {

    private Long id;

    private String name;

    private Long workflowId;

    private String type;

    private String subType;

    private String keyPattern;

    private String filterPattern;

    private String aggPattern;

    private String valueType;

}
