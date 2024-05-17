-- titanium.netconthourly definition

CREATE TABLE `netconthourly` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `interface_name` varchar(100) NOT NULL COMMENT '接口名称',
                                 `bytes_received` bigint unsigned NOT NULL COMMENT '接收字节数',
                                 `bytes_sent` bigint unsigned NOT NULL COMMENT '发送字节数',
                                 `created_at` timestamp NOT NULL COMMENT '记录创建时间戳',
                                 `hour` datetime NOT NULL COMMENT '统计小时',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `netconthourly_unique` (`hour`)
) ENGINE=InnoDB AUTO_INCREMENT=593 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='网络每天流量统计';

-- titanium.netcontdaily definition

CREATE TABLE `netcontdaily` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `interface_name` varchar(100) NOT NULL COMMENT '接口名称',
                                `period_start` time NOT NULL COMMENT '统计起始时间',
                                `bytes_received` bigint unsigned NOT NULL COMMENT '发送字节数',
                                `bytes_sent` bigint unsigned NOT NULL COMMENT '接收字节数',
                                `created_at` timestamp NOT NULL COMMENT '记录创建时间戳',
                                `date` date NOT NULL COMMENT '统计日期',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `netcontdaily_unique` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='网络接口每日流量统计';