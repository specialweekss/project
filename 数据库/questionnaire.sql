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

 Date: 27/06/2024 18:44:12
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
) ENGINE = InnoDB AUTO_INCREMENT = 207 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

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
INSERT INTO `answer` VALUES (198, 146, 126, 0, '1', 0);
INSERT INTO `answer` VALUES (199, 146, 127, 0, '2', 0);
INSERT INTO `answer` VALUES (200, 147, 128, 0, '2', 0);
INSERT INTO `answer` VALUES (201, 157, 138, 0, '2', 0);
INSERT INTO `answer` VALUES (202, 157, 136, 0, '2', 0);
INSERT INTO `answer` VALUES (203, 158, 147, 0, '2', 0);
INSERT INTO `answer` VALUES (204, 159, 147, 0, '2', 0);
INSERT INTO `answer` VALUES (205, 163, 147, 0, '2', 0);
INSERT INTO `answer` VALUES (206, 167, 157, 0, '1', 1);
INSERT INTO `answer` VALUES (207, 168, 157, 2, '1', 1);
INSERT INTO `answer` VALUES (208, 171, 163, 1, '12', 0);
INSERT INTO `answer` VALUES (209, 172, 163, 2, '1', 0);
INSERT INTO `answer` VALUES (210, 173, 163, 3, '空', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 168 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (167, 1, '问题', 0, 0, 327, 1, 3);
INSERT INTO `question` VALUES (168, 2, '问题标题', 0, 2, 327, 1, 0);
INSERT INTO `question` VALUES (175, 1, '问题标题', 0, 0, 331, 0, 2);

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
) ENGINE = InnoDB AUTO_INCREMENT = 328 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of questionnaire
-- ----------------------------
INSERT INTO `questionnaire` VALUES (327, 'nih', 157, 1, '2024-06-26 11:40:41', '2024-06-27 00:00:00', 2, 1);
INSERT INTO `questionnaire` VALUES (331, '你好', 165, 1, '2024-06-27 18:37:00', '2024-06-29 00:00:00', 1, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (83, 147, 323, '2024-06-26 10:31:10', 1, '你好');
INSERT INTO `record` VALUES (84, 147, 323, '2024-06-26 10:31:59', 1, '你好');
INSERT INTO `record` VALUES (85, 147, 324, '2024-06-26 10:37:55', 1, '你好');
INSERT INTO `record` VALUES (86, 157, 327, '2024-06-26 11:41:32', 0, 'nih');
INSERT INTO `record` VALUES (87, 163, 329, '2024-06-27 17:33:44', 1, '问卷');

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
) ENGINE = InnoDB AUTO_INCREMENT = 270 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of selection
-- ----------------------------
INSERT INTO `selection` VALUES (268, 167, 1, '选项');
INSERT INTO `selection` VALUES (269, 167, 2, '选项');
INSERT INTO `selection` VALUES (270, 167, 3, '选项');
INSERT INTO `selection` VALUES (280, 175, 1, '选项');
INSERT INTO `selection` VALUES (281, 175, 2, '选项');

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
) ENGINE = InnoDB AUTO_INCREMENT = 163 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (102, '管理员', 1, 'aD123');
INSERT INTO `user` VALUES (103, '管理', 1, '111');
INSERT INTO `user` VALUES (147, '145', 0, 'aD123');
INSERT INTO `user` VALUES (151, '110', 0, 'aD123');
INSERT INTO `user` VALUES (152, '147', 0, 'aD123');
INSERT INTO `user` VALUES (153, '1471', 0, 'aD123');
INSERT INTO `user` VALUES (154, '14711', 0, 'aD123');
INSERT INTO `user` VALUES (155, '1472', 0, 'aD123');
INSERT INTO `user` VALUES (156, '1111', 0, 'aD123');
INSERT INTO `user` VALUES (157, 'nih ', 0, 'aD12');
INSERT INTO `user` VALUES (158, '666', 0, 'Lin777');
INSERT INTO `user` VALUES (159, '你好', 0, 'L4d2');
INSERT INTO `user` VALUES (161, 'a', 0, 'aD123');
INSERT INTO `user` VALUES (162, '1', 0, 'aD123');
INSERT INTO `user` VALUES (165, '111', 0, 'aD123');

SET FOREIGN_KEY_CHECKS = 1;
