CREATE TABLE `traffic` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `day_limit` int DEFAULT NULL COMMENT '每天限制多少条，短链',
  `day_used` int DEFAULT NULL COMMENT '当天用了多少条，短链',
  `total_limit` int DEFAULT NULL COMMENT '总次数，活码才用',
  `account_no` bigint DEFAULT NULL COMMENT '账号',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `level` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品层级：FIRST青铜、SECOND黄金、THIRD钻石',
  `expired_date` date DEFAULT NULL COMMENT '过期日期',
  `plugin_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '插件类型',
  `product_id` bigint DEFAULT NULL COMMENT '商品主键',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_trade_no` (`out_trade_no`,`account_no`) USING BTREE,
  KEY `idx_account_no` (`account_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;