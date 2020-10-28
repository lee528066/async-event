CREATE TABLE IF NOT EXISTS `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) NOT NULL COMMENT '事件名称',
  `unique_key` varchar(256) NOT NULL COMMENT '唯一key',
  `bean_class` varchar(256) NOT NULL COMMENT '执行类的命名空间',
  `bill_content` varchar(256) NOT NULL,
  `method_name` varchar(45) NOT NULL COMMENT '执行方法',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL COMMENT '状态',
  `error_reason` varchar(2000) DEFAULT NULL COMMENT '失败原因',
  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败次数',
  `param_pairs` varchar(4096) NOT NULL COMMENT '参数值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueKey_UNIQUE` (`unique_key`),
  UNIQUE KEY `uk_context_class_method` (`bill_content`,`bean_class`,`method_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8