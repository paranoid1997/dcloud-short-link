CREATE TABLE `traffic_task` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `account_no` bigint DEFAULT NULL,
  `traffic_id` bigint DEFAULT NULL,
  `use_times` int DEFAULT NULL,
  `lock_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '锁定状态锁定LOCK  完成FINISH-取消CANCEL',
  `message_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '唯一标识',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_msg_id` (`message_id`) USING BTREE,
  KEY `idx_release` (`account_no`,`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;