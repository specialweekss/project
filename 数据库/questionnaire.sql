/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80037
 Source Host           : localhost:3306
 Source Schema         : questionnaire

 Target Server Type    : MySQL
 Target Server Version : 80037
 File Encoding         : 65001

 Date: 21/06/2024 21:18:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `answer_id` int NOT NULL AUTO_INCREMENT COMMENT '答案id',
  `question_id` int NULL DEFAULT NULL COMMENT '答案对应的问题',
  `user_id` int NULL DEFAULT NULL COMMENT '回答者id',
  `type` int NULL DEFAULT NULL COMMENT '答案类型，1填空，2单选，3多选，4打分',
  `answer` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '答案，单选多选用字符串储存',
  `is_valid` int NOT NULL DEFAULT 1 COMMENT '是否有效（问题修改后无效），1有效，0无效',
  PRIMARY KEY (`answer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 157 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES (181, 137, 110, 0, '1', 0);
INSERT INTO `answer` VALUES (182, 137, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (183, 137, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (184, 137, 110, 0, '1', 0);
INSERT INTO `answer` VALUES (185, 137, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (186, 137, 110, 0, '1', 0);
INSERT INTO `answer` VALUES (187, 137, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (188, 137, 110, 0, '1', 0);
INSERT INTO `answer` VALUES (189, 137, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (190, 138, 110, 2, '1', 0);
INSERT INTO `answer` VALUES (191, 138, 110, 2, '2', 0);
INSERT INTO `answer` VALUES (192, 139, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (193, 138, 110, 2, '3', 0);
INSERT INTO `answer` VALUES (194, 139, 110, 0, '1', 0);
INSERT INTO `answer` VALUES (195, 138, 110, 2, '1', 0);
INSERT INTO `answer` VALUES (196, 139, 110, 0, '2', 0);
INSERT INTO `answer` VALUES (197, 140, 110, 1, '12', 0);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `question_id` int NOT NULL AUTO_INCREMENT COMMENT '问题id',
  `number` int NULL DEFAULT NULL COMMENT '问题在问卷中的编号',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '问题标题',
  `necessary` int NOT NULL DEFAULT 1 COMMENT '是否必选，1是，0否',
  `type` int NULL DEFAULT 0 COMMENT '问题类型，0单选，1多选，2打分，3填空',
  `questionnaire_id` int NULL DEFAULT NULL COMMENT '问卷id',
  `answer_num` int NOT NULL DEFAULT 0 COMMENT '答案数量',
  `selection_num` int NOT NULL DEFAULT 0 COMMENT '选项数量，填空打分题无需填入，默认0',
  PRIMARY KEY (`question_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------

-- ----------------------------
-- Table structure for questionnaire
-- ----------------------------
DROP TABLE IF EXISTS `questionnaire`;
CREATE TABLE `questionnaire`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '问卷id',
  `theme` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '问卷标题',
  `creator_id` int NULL DEFAULT NULL COMMENT '创建者id',
  `state` int NOT NULL DEFAULT 0 COMMENT '问卷状态，0未发布，1已发布，2截止',
  `release_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `question_num` int NOT NULL DEFAULT 0 COMMENT '问题数',
  `commit_num` int NOT NULL DEFAULT 0 COMMENT '填写的问卷数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 242 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of questionnaire
-- ----------------------------

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '填写记录id',
  `user_id` int NOT NULL COMMENT '用户id',
  `questionnaire_id` int NOT NULL COMMENT '问卷id',
  `record_time` datetime NOT NULL COMMENT '填写时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '（废弃字段）是否被用户删除，0否1是，但记录依然存在（可被创建者/管理员查看）',
  `theme` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '问卷标题',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (57, 110, 264, '2024-06-21 16:57:29', 0, NULL);
INSERT INTO `record` VALUES (58, 110, 264, '2024-06-21 16:57:33', 0, NULL);
INSERT INTO `record` VALUES (59, 110, 264, '2024-06-21 17:23:53', 0, '你好');
INSERT INTO `record` VALUES (60, 110, 264, '2024-06-21 17:23:56', 0, '你好');
INSERT INTO `record` VALUES (61, 110, 264, '2024-06-21 17:39:37', 0, '你好');
INSERT INTO `record` VALUES (62, 110, 264, '2024-06-21 17:39:39', 0, '你好');
INSERT INTO `record` VALUES (63, 110, 264, '2024-06-21 17:40:48', 0, '你好');
INSERT INTO `record` VALUES (64, 110, 264, '2024-06-21 17:40:50', 0, '你好');
INSERT INTO `record` VALUES (65, 110, 264, '2024-06-21 17:48:27', 0, '你好');
INSERT INTO `record` VALUES (66, 110, 264, '2024-06-21 17:52:43', 0, '你好');
INSERT INTO `record` VALUES (67, 110, 264, '2024-06-21 17:53:36', 0, '你好');
INSERT INTO `record` VALUES (68, 110, 264, '2024-06-21 17:53:40', 0, '你好');
INSERT INTO `record` VALUES (69, 110, 264, '2024-06-21 17:54:07', 0, '你好');

-- ----------------------------
-- Table structure for selection
-- ----------------------------
DROP TABLE IF EXISTS `selection`;
CREATE TABLE `selection`  (
  `selection_id` int NOT NULL AUTO_INCREMENT COMMENT '选项id',
  `question_id` int NOT NULL COMMENT '问题id',
  `position` int NOT NULL DEFAULT 1 COMMENT '选项编号，1表示A,2表示B等等',
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '选项文本',
  PRIMARY KEY (`selection_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of selection
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户的id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `type` int NULL DEFAULT NULL COMMENT '用户类型，0普通用户，1管理员',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (102, '管理员', 1, '111');
INSERT INTO `user` VALUES (110, '11', 0, 'aD123');
INSERT INTO `user` VALUES (112, '111', 0, 'aD123');
INSERT INTO `user` VALUES (113, '1111111', 0, 'aD123');

SET FOREIGN_KEY_CHECKS = 1;
