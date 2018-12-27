
CREATE TABLE `user_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOGIN_NAME` varchar(45) NOT NULL COMMENT '登录名',
  `PASSWORD` varchar(45) NOT NULL COMMENT '登陆密码',
  `ROLE` smallint(6) NOT NULL DEFAULT '2' COMMENT '用户权限的代表数字(0表示系统管理员，1表示数据库管理员，2表示普通用户)',
  `CREATE_BY` varchar(45) NOT NULL DEFAULT 'admin' COMMENT '数据创建人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `UPDATE_BY` varchar(45) NOT NULL DEFAULT 'admin' COMMENT '数据更新人',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `LOGIN_NAME_UNIQUE` (`LOGIN_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户登陆信息表';


CREATE TABLE `swagger_api` (
  `SERVICE_NAME` varchar(45) NOT NULL COMMENT '服务的名称',
  `API_DOCS` mediumtext NOT NULL COMMENT '服务对应的swagger api-docs',
  `NOT_STANDARD_NUM` int(11) NOT NULL DEFAULT '0' COMMENT '对接swagger不规范的数目',
  `CREATE_BY` varchar(45) NOT NULL DEFAULT 'auto_scan' COMMENT '数据记录创建人',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATE_BY` varchar(45) DEFAULT 'auto_scan' COMMENT '数据记录更新人',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据更新时间',
  PRIMARY KEY (`SERVICE_NAME`),
  UNIQUE KEY `SERVICE_NAME_UNIQUE` (`SERVICE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存放swagger api的表';
