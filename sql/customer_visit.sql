/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : ry-cloud

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 17/04/2026
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer_visit
-- ----------------------------
DROP TABLE IF EXISTS `customer_visit`;
CREATE TABLE `customer_visit`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `customer_id` int NOT NULL DEFAULT 0 COMMENT '客户ID',
  `user_id` int NULL DEFAULT 0 COMMENT '用户ID',
  `visit_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '回访内容',
  `visit_date` date NULL DEFAULT NULL COMMENT '回访时间',
  `feedback` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '客户反馈',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` int NULL DEFAULT 0 COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` int NULL DEFAULT 0 COMMENT '修改人',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户回访记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
