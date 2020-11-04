CREATE TABLE `antibot_strategy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `model_id` bigint(20) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL COMMENT '策略名称',
  `code` varchar(45) DEFAULT NULL,
  `high` int(11) DEFAULT NULL COMMENT '高风险阀值',
  `mid` int(11) DEFAULT NULL COMMENT '中风险阀值',
  `owner` varchar(45) DEFAULT NULL COMMENT '负责人',
  `depart` varchar(45) DEFAULT NULL COMMENT '部门',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_stream_data_sources` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `address` varchar(1024) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
  `sub_group` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(45) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_stream_variables` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) DEFAULT NULL COMMENT '名称',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流ID',
  `type` int(11) DEFAULT NULL COMMENT '变量类型',
  `sub_type` int(11) DEFAULT NULL,
  `key_pattern` varchar(255) DEFAULT NULL,
  `filter_pattern` varchar(255) DEFAULT NULL,
  `value_type` varchar(45) DEFAULT NULL COMMENT '数据类型',
  `agg_pattern` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(45) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_stream_workflows` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `ds_id` bigint(20) DEFAULT NULL,
  `pk_pattern` varchar(255) DEFAULT NULL COMMENT '主键表达式',
  `etl_pattern` varchar(255) DEFAULT NULL COMMENT 'ETL表达式',
  `ts_pattern` varchar(255) DEFAULT NULL COMMENT '时间戳表达式',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `def_value` varchar(45) DEFAULT NULL COMMENT '变量默认值',
  `source_id` varchar(45) DEFAULT NULL COMMENT '原始变量ID，如ROLLUP对应的STREAMING变量',
  `logic_expr` varchar(256) DEFAULT NULL COMMENT 'OTF的计算逻辑表达式',
  `key_expr` varchar(256) DEFAULT NULL COMMENT '查询KEY的计算逻辑表达式',
  `period` int(11) DEFAULT NULL COMMENT 'ROLLUP变量的时间长度',
  `time_unit` varchar(45) DEFAULT NULL COMMENT 'ROLLUP变量的时间单位',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL COMMENT '模型名称',
  `remark` varchar(1024) DEFAULT NULL COMMENT '模型描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_model_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `model_id` bigint(20) DEFAULT NULL COMMENT '模型id',
  `code` varchar(45) DEFAULT NULL COMMENT '属性编码',
  `name` varchar(256) DEFAULT NULL COMMENT '属性名称',
  `data_type` varchar(45) DEFAULT NULL COMMENT '数据类型',
  `required` tinyint(4) DEFAULT NULL COMMENT '是否必填',
  `def_value` varchar(45) DEFAULT NULL COMMENT '默认值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(45) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_namelist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '名单名称',
  `remark` varchar(45) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_namelist_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namelist_id` bigint(20) DEFAULT NULL COMMENT '名单列表ID',
  `key` varchar(256) DEFAULT NULL,
  `reason` varchar(256) DEFAULT NULL COMMENT '原因',
  `type` int(11) DEFAULT NULL COMMENT '查询KEY',
  `effect_date` timestamp NULL DEFAULT NULL COMMENT '生效时间',
  `expire_date` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` bigint(20) DEFAULT NULL COMMENT '策略ID',
  `name` varchar(256) DEFAULT NULL COMMENT '规则名称',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `score` int(11) DEFAULT NULL COMMENT '得分',
  `status` tinyint(4) DEFAULT NULL COMMENT '规则状态，1表示启用，0表示禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_rule_condition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则ID',
  `variable_id` bigint(20) DEFAULT NULL COMMENT '变量ID',
  `operation` varchar(45) DEFAULT NULL COMMENT '比较操作的编码',
  `factor` varchar(45) DEFAULT NULL COMMENT '操作因子',
  `pre_id` bigint(20) DEFAULT NULL COMMENT '与上一个条件之间的关系',
  `relation` varchar(45) DEFAULT NULL COMMENT '与上一个条件之间的关系',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_detect_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` bigint(20) DEFAULT NULL COMMENT '策略ID',
  `strategy_code` varchar(45) DEFAULT NULL COMMENT '策略编码',
  `host` varchar(45) DEFAULT NULL COMMENT '主机',
  `risk_level` int(11) DEFAULT NULL COMMENT '风险等级',
  `score` int(11) DEFAULT NULL COMMENT '总分值',
  `cost_time` bigint(20) DEFAULT NULL COMMENT '耗时',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `antibot_detect_log_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` bigint(20) DEFAULT NULL COMMENT '策略ID',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则ID',
  `score` int(11) DEFAULT NULL COMMENT '得分',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

