SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
INSERT INTO `short_link_0` VALUES (1469558440845115394, NULL, NULL, NULL, NULL, 'a4vBfuS0', 'E17DC8ACB57C574EE7C68271FD66A803', NULL, 48621039, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
INSERT INTO `short_link_0` VALUES (1469558441121939458, NULL, NULL, NULL, NULL, 'a2ZaGUN0', '6CAF6B9E58FFB39C101A4D83340C4F9A', NULL, 87827473, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
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
INSERT INTO `short_link_a` VALUES (1469558436688560129, NULL, NULL, NULL, NULL, 'a1uNJXka', '47491F0075C9E8C8D7F13E7C830D7C4F', NULL, 25897582, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
INSERT INTO `short_link_a` VALUES (1469558440668954626, NULL, NULL, NULL, NULL, 'a24N0tWa', 'A46AFC7A5F9D385A956DFF46DB6F933D', NULL, 44461517, '2021-12-11 14:43:27', '2021-12-11 14:43:27', 0, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
