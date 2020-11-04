package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddWorkflowDTO {

    private Long id;

    private String name;

    private String type;

    private Long dataSourceId;

    private String pkPattern;

    private String etlPattern;

    private String tsPattern;

    private List<StreamVariableDTO> variables;

}
