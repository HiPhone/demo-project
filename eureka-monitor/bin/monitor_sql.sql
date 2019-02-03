DROP TABLE IF EXISTS eureka_applications;
DROP TABLE IF EXISTS eureka_instances;
DROP TABLE IF EXISTS eureka_historys;

CREATE TABLE `eureka_applications` (
  `CLUSTER_ID` varchar(20) NOT NULL COMMENT '集群Id',
  `APPLICATION_NAME` varchar(100) NOT NULL COMMENT '应用名称',
  `UP_NUM` int(11) DEFAULT NULL COMMENT '状态为UP的instance数目',
  `TOTAL_NUM` int(11) DEFAULT NULL COMMENT '全部instance的数目',
  PRIMARY KEY (`CLUSTER_ID`,`APPLICATION_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eureka_instances` (
  `INSTANCE_ID` varchar(100) NOT NULL COMMENT '服务实例在eureka中的唯一的instance id',
  `CLUSTER_ID` varchar(10) NOT NULL COMMENT 'eureka集群的id',
  `APPLICATION_NAME` varchar(45) NOT NULL COMMENT '应用的名称',
  `HOST_NAME` varchar(45) NOT NULL COMMENT '应用所在的服务器的hostname',
  `IP_ADDRESS` char(15) NOT NULL COMMENT '应用所属在服务器的ip地址',
  `SERVICE_PORT` int(5) NOT NULL COMMENT '应用部署占用的端口',
  `CURRENT_STATE` tinyint(4) NOT NULL COMMENT '应用上次的状态 0 - UP, 1 - DOWN',
  `REGISTER_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '应用注册到eureka的时间\\n',
  `DOWN_TIME` timestamp NULL DEFAULT NULL COMMENT '应用从eureka取消注册的时间，默认值为new Date(0)',
  PRIMARY KEY (`INSTANCE_ID`,`CLUSTER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eureka_historys` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增的历史记录id',
  `CLUSTER_ID` varchar(15) NOT NULL COMMENT '历史记录集群的id',
  `APPLICATION_NAME` varchar(45) NOT NULL COMMENT '记录的应用的名称',
  `IP_ADDRESS` char(21) NOT NULL COMMENT '记录的ip地址',
  `STATE` tinyint(4) NOT NULL COMMENT '记录的状态',
  `LOG_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `id_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='eureka状态变更历史记录表';
