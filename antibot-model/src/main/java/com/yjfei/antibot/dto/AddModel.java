package com.yjfei.antibot.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddModel {
    private Long id;

    private String name;

    private String remark;

    private List<ModelFieldDTO> fields;

}
