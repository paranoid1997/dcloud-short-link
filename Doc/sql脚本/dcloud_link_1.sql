SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for group_code_mapping_0
-- ----------------------------
DROP TABLE IF EXISTS `group_code_mapping_0`;
CREATE TABLE `group_code_mapping_0` (
  `id` bigint unsigned NOT NULL,
  `group_id` bigint DEFAULT NULL COMMENT '组',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链标题',
  `original_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始url地址',
  `domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链域名',
  `code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '短链压缩码',
  `sign` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '长链的md5码，方便查找',
  `expired` datetime DEFAULT NULL COMMENT '过期时间，长久就是-1',
  `account_no` bigint DEFAULT NULL COMMENT '账号唯一编号',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del` int unsigned NOT NULL COMMENT '0是默认，1是删除',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态，lock是锁定不可用，active是可用',
  `link_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of group_code_mapping_0
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for group_code_mapping_1
-- ----------------------------
DROP TABLE IF EXISTS `group_code_mapping_1`;
CREATE TABLE `group_code_mapping_1` (
  `id` bigint unsigned NOT NULL,
  `group_id` bigint DEFAULT NULL COMMENT '组',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链标题',
  `original_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始url地址',
  `domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链域名',
  `code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '短链压缩码',
  `sign` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '长链的md5码，方便查找',
  `expired` datetime DEFAULT NULL COMMENT '过期时间，长久就是-1',
  `account_no` bigint DEFAULT NULL COMMENT '账号唯一编号',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del` int unsigned NOT NULL COMMENT '0是默认，1是删除',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态，lock是锁定不可用，active是可用',
  `link_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of group_code_mapping_1
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for link_group
-- ----------------------------
DROP TABLE IF EXISTS `link_group`;
CREATE TABLE `link_group` (
  `id` bigint unsigned NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组名',
  `account_no` bigint DEFAULT NULL COMMENT '账号唯一编号',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of link_group
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for short_link_0
-- ----------------------------
DROP TABLE IF EXISTS `short_link_0`;
CREATE TABLE `short_link_0` (
  `id` bigint unsigned NOT NULL,
  `group_id` bigint DEFAULT NULL COMMENT '组',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链标题',
  `original_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始url地址',
  `domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链域名',
  `code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '短链压缩码',
  `sign` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '长链的md5码，方便查找',
  `expired` datetime DEFAULT NULL COMMENT '过期时间，长久就是-1',
  `account_no` bigint DEFAULT NULL COMMENT '账号唯一编号',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del` int unsigned NOT NULL COMMENT '0是默认，1是删除',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态，lock是锁定不可用，active是可用',
  `link_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of short_link_0
-- ----------------------------
BEGIN;
INSERT INTO `short_link_0` VALUES (1469558440337604610, NULL, NULL, NULL, NULL, '12GVs4s0', 'F0F6FE3A7F9DF7017DBE5750BA352B6A', NULL, 73857461, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for short_link_a
-- ----------------------------
DROP TABLE IF EXISTS `short_link_a`;
CREATE TABLE `short_link_a` (
  `id` bigint unsigned NOT NULL,
  `group_id` bigint DEFAULT NULL COMMENT '组',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链标题',
  `original_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原始url地址',
  `domain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链域名',
  `code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '短链压缩码',
  `sign` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '长链的md5码，方便查找',
  `expired` datetime DEFAULT NULL COMMENT '过期时间，长久就是-1',
  `account_no` bigint DEFAULT NULL COMMENT '账号唯一编号',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del` int unsigned NOT NULL COMMENT '0是默认，1是删除',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态，lock是锁定不可用，active是可用',
  `link_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of short_link_a
-- ----------------------------
BEGIN;
INSERT INTO `short_link_a` VALUES (1469557545176707073, NULL, NULL, NULL, NULL, '1183LgNa', 'FBA41AABB06C2A243E4609F461AFBCB3', NULL, 73304748, '2021-12-11 14:42:20', '2021-12-11 14:42:20', 0, NULL, NULL);
INSERT INTO `short_link_a` VALUES (1469558439511326722, NULL, NULL, NULL, NULL, '13FDrKja', 'A01FD16D8B36A19CFC40DBD7BFF527FD', NULL, 49754981, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
INSERT INTO `short_link_a` VALUES (1469558439905591297, NULL, NULL, NULL, NULL, '12RUju3a', '46EBCDE46E56573FEE8CBDF4A18A2BF6', NULL, 62072431, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
INSERT INTO `short_link_a` VALUES (1469558440970944514, NULL, NULL, NULL, NULL, '114ha8Ma', 'C21DFB4DDF66B3BF8801470528AABAE0', NULL, 88342456, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
