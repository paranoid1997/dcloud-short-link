SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `account_no` bigint DEFAULT NULL,
  `head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '盐，用于个人敏感信息处理',
  `mail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名',
  `auth` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '认证级别，DEFAULT，REALNAME，ENTERPRISE，访问次数不一样',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`) USING BTREE,
  UNIQUE KEY `uk_account` (`account_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of account
-- ----------------------------
BEGIN;
INSERT INTO `account` VALUES (11, 693100647796441088, 'https://dcloud-link.oss-cn-guangzhou.aliyuncs.com/user/2021/11/18/4e5bcb2327ea4a94b607b6272cb1b8dd.png', '13113777337', '$1$Ix2b9oNG$QVLe0ws5xJwopKisubajJ0', '$1$Ix2b9oNG', NULL, '小滴v1', 'DEFAULT', '2022-01-26 14:14:35', '2022-01-26 14:14:35');
INSERT INTO `account` VALUES (12, 1643354298455, '', '15529052608', '$1$0cymioeh$CoYp5.mb3I0d0iyb/emr//', '$1$0cymioeh', '', 'duanmouren', NULL, '2022-01-28 15:18:19', '2022-01-28 15:18:19');
INSERT INTO `account` VALUES (13, 697547303568801792, '', '15529052603', '$1$FnHFsFIZ$8Tv/ScqQM8YRUdJBoY/HY/', '$1$FnHFsFIZ', '', 'duanmouren', NULL, '2022-02-07 20:43:56', '2022-02-07 20:43:56');
INSERT INTO `account` VALUES (14, 697548723361677312, '', '15529052604', '$1$AeHbZK9F$2dYR3.OPuF6HL6uhHS7tk.', '$1$AeHbZK9F', '', 'duanmouren', NULL, '2022-02-07 20:49:34', '2022-02-07 20:49:34');
COMMIT;

-- ----------------------------
-- Table structure for traffic
-- ----------------------------
DROP TABLE IF EXISTS `traffic`;
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

-- ----------------------------
-- Records of traffic
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for traffic_0
-- ----------------------------
DROP TABLE IF EXISTS `traffic_0`;
CREATE TABLE `traffic_0` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1486221880318595080 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of traffic_0
-- ----------------------------
BEGIN;
INSERT INTO `traffic_0` VALUES (1486221880318595073, 5, 5, NULL, 693100647796441088, 'zxABhkLEGrGvmSeSbx23COfU71yj7nwN', 'SECOND', '2022-04-25', 'SHORT_LINK', 2, '2022-01-26 14:18:06', '2022-02-16 16:03:21');
INSERT INTO `traffic_0` VALUES (1486221880318595076, 2, 2, NULL, 693100647796441088, 'free_init', 'FIRST', '2022-01-11', 'SHORT_LINK', 1, '2022-02-14 11:31:02', '2022-02-16 16:03:35');
INSERT INTO `traffic_0` VALUES (1486221880318595079, 2, 0, NULL, 693100647796441082, 'free_init', 'FIRST', '2022-01-11', 'SHORT_LINK', 1, '2022-02-14 11:31:02', '2022-02-14 11:31:50');
COMMIT;

-- ----------------------------
-- Table structure for traffic_1
-- ----------------------------
DROP TABLE IF EXISTS `traffic_1`;
CREATE TABLE `traffic_1` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1480096938903965699 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of traffic_1
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for traffic_task
-- ----------------------------
DROP TABLE IF EXISTS `traffic_task`;
CREATE TABLE `traffic_task` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `account_no` bigint DEFAULT NULL,
  `traffic_id` bigint DEFAULT NULL,
  `use_times` int DEFAULT NULL,
  `lock_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '锁定状态锁定LOCK  完成FINISH-取消CANCEL',
  `biz_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '唯一标识',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_id` (`biz_id`) USING BTREE,
  KEY `idx_release` (`account_no`,`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of traffic_task
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
