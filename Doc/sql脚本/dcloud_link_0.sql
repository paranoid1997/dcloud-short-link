SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for domain
-- ----------------------------
DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain` (
  `id` bigint unsigned NOT NULL,
  `account_no` bigint DEFAULT NULL COMMENT '用户自己绑定的域名',
  `domain_type` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '域名类型，自建custom, 官方offical',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `del` int(1) unsigned zerofill DEFAULT '0' COMMENT '0是默认，1是禁用',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of domain
-- ----------------------------
BEGIN;
COMMIT;

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
INSERT INTO `link_group` VALUES (1468878230818746370, '测试水平分库分组0', 1637241966000, '2021-12-09 17:40:34', '2021-12-09 17:40:34');
INSERT INTO `link_group` VALUES (1468878433382658050, '测试水平分库分组1', 1637241966000, '2021-12-09 17:41:22', '2021-12-09 17:41:22');
INSERT INTO `link_group` VALUES (1468878444216545282, '测试水平分库分组2', 1637241966000, '2021-12-09 17:41:24', '2021-12-09 17:41:24');
INSERT INTO `link_group` VALUES (1468878456170311682, '测试水平分库分组3', 1637241966000, '2021-12-09 17:41:27', '2021-12-09 17:41:27');
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
INSERT INTO `short_link_0` VALUES (1469558440480210946, NULL, NULL, NULL, NULL, '029OD4Q0', 'C1876D205BF3C10AC14D6453A44A61FD', NULL, 99363517, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
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
INSERT INTO `short_link_a` VALUES (1469558440039809025, NULL, NULL, NULL, NULL, '04DzCFLa', 'E00F7393582CBAEFAA5D88B10DBB0FEC', NULL, 46978791, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
