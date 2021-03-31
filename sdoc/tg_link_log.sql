/*
 Navicat Premium Data Transfer

 Source Server         : songchaoming
 Source Server Type    : MySQL
 Source Server Version : 50639
 Source Host           : localhost:3306
 Source Schema         : manage

 Target Server Type    : MySQL
 Target Server Version : 50639
 File Encoding         : 65001

 Date: 23/03/2021 18:32:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tg_link_log
-- ----------------------------
DROP TABLE IF EXISTS `tg_link_log`;
CREATE TABLE `tg_link_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tg_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '推广链接',
  `effective` tinyint(1) NULL DEFAULT NULL COMMENT '是否有效0有效1无效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
