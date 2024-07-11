
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aq_message
-- ----------------------------
DROP TABLE IF EXISTS `aq_message`;
CREATE TABLE `aq_message`  (
                               `message_id` varchar(128) NOT NULL COMMENT '消息id',
                               `room_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '房间id',
                               `sender_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户id',
                               `message_type` int NOT NULL DEFAULT 0 COMMENT '消息类型',
                               `message_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
                               `message_ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息扩展字段',
                               `status` tinyint NOT NULL DEFAULT 1 COMMENT '消息状态 0不显示 1显示',
                               `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                               PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aq_message
-- ----------------------------

-- ----------------------------
-- Table structure for aq_user
-- ----------------------------
DROP TABLE IF EXISTS `aq_user`;
CREATE TABLE `aq_user`  (
                            `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                            `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
                            `user_avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
                            PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aq_user
-- ----------------------------
INSERT INTO `aq_user` VALUES ('AQChatHelper', 'AQChat助手', 'https://aqchat.oss-cn-shenzhen.aliyuncs.com/avatar/AQChatAI.png');
INSERT INTO `aq_user` VALUES ('xm', '小M', 'shenzhen.aliyuncs.com/avatar/xm.png');
INSERT INTO `aq_user` VALUES ('xt', '小T', 'shenzhen.aliyuncs.com/avatar/xt.png');
INSERT INTO `aq_user` VALUES ('xv', '小V', 'shenzhen.aliyuncs.com/avatar/xv.png');

SET FOREIGN_KEY_CHECKS = 1;