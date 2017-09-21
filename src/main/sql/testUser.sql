/*
Navicat MySQL Data Transfer

Source Server         : 腾讯云
Source Server Version : 50711
Source Host           : 119.29.192.62:3306
Source Database       : neubbs

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-09-21 14:26:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_name` varchar(15) DEFAULT NULL,
  `u_password` varchar(15) NOT NULL,
  `u_sex` varchar(2) DEFAULT NULL,
  `u_birthday` varchar(20) DEFAULT NULL,
  `u_address` varchar(15) DEFAULT NULL,
  `u_phone` varchar(15) DEFAULT NULL,
  `u_email` varchar(50) NOT NULL,
  `u_registertime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `u_name` (`u_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '第一个用户', '12345', null, null, null, null, 'test@test.com', '2017-09-21 14:26:13');
