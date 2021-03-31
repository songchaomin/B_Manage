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

 Date: 23/03/2021 18:30:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_count
-- ----------------------------
DROP TABLE IF EXISTS `sys_count`;
CREATE TABLE `sys_count`  (
  `member_no` bigint(20) NOT NULL,
  PRIMARY KEY (`member_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

insert into sys_count (member_no) values(90000000)
SET FOREIGN_KEY_CHECKS = 1;
