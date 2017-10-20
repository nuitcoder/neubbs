/*
Navicat MySQL Data Transfer

Source Server         : liushuwei
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : neubbs

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-09-22 14:21:10
*/

-- MySQL中取消外键约束
SET FOREIGN_KEY_CHECKS=0;


-- 删除原有表
DROP TABLE IF EXISTS `forum_user`;
DROP TABLE IF EXISTS `forum_topic`;
DROP TABLE IF EXISTS `forum_topic_content`;
DROP TABLE IF EXISTS `forum_topic_reply`;

-- 论坛用户
CREATE TABLE `forum_user` (
  `fu_id` INT(11) NOT NULL AUTO_INCREMENT,
  `fu_name` VARCHAR(15) DEFAULT NULL,
  `fu_password` VARCHAR(100) NOT NULL,
  `fu_email` VARCHAR(30) NOT NULL,
  `fu_sex` VARCHAR(2) DEFAULT NULL,
  `fu_birthday` VARCHAR(20) DEFAULT NULL,
  `fu_address` VARCHAR(100) DEFAULT NULL,
  `fu_description` VARCHAR(50) DEFAULT NULL comment '一句话描述',
  `fu_personalprofile` VARCHAR(255) DEFAULT NULL comment '个人简介',
  `fu_image` VARCHAR(100) comment '用户头像地址',
  `fu_rank` VARCHAR(10) DEFAULT 'user' comment '用户级别',
  `fu_state` INT(1) NOT NULL DEFAULT 0 comment '激活状态',
  `fu_createtime` datetime DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`fu_id`),
   UNIQUE KEY `fu_name` (`fu_name`),
   UNIQUE KEY `fu_email` (`fu_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 论坛主题
CREATE TABLE `forum_topic` (
  `ft_id` INT(11) NOT NULL AUTO_INCREMENT,
  `fu_id` INT(11) NOT NULL comment '发表人id',
  `ft_category` VARCHAR(20) NOT NULL comment '话题分类',
  `ft_title` VARCHAR(50) NOT NULL,
  `ft_comment` VARCHAR(10) DEFAULT '0',
  `ft_lastreplyuserid` INT(11) DEFAULT NULL comment '最后回复人id',
  `ft_lastreplytime` DATETIME DEFAULT NULL comment '最后回复时间',
  `ft_createtime` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ft_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 论坛主题内容
CREATE TABLE `forum_topic_content` (
  `ftc_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ft_id` INT(11) DEFAULT NULL comment '对应话题id',
  `ftc_content` longtext NOT NULL,
  `ftc_read` VARCHAR(10) DEFAULT '0' comment '阅读数',
  `ftc_agree` VARCHAR(10) DEFAULT '0' comment '点攒数',
  PRIMARY KEY (`ftc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 论坛主题回复
CREATE TABLE `forum_topic_reply` (
  `ftr_id` INT(11) NOT NULL AUTO_INCREMENT,
  `fu_id` INT(11) DEFAULT NULL comment '回复人id',
  `ft_id` INT(11) DEFAULT NULL comment '对应话题id',
  `ftr_content` VARCHAR(150) NOT NULL,
  `ftr_agree` VARCHAR(10) DEFAULT '0',
  `ftr_oppose` VARCHAR(10) DEFAULT '0',
  `ftr_createtime` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ftr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

