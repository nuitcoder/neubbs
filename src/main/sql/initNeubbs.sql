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
  `fu_password` VARCHAR(15) NOT NULL,
  `fu_sex` VARCHAR(2) DEFAULT NULL,
  `fu_birthday` VARCHAR(20) DEFAULT NULL,
  `fu_phone` VARCHAR(15) DEFAULT NULL,
  `fu_email` VARCHAR(50) NOT NULL,
  `fu_address` VARCHAR(50) DEFAULT NULL,
  `fu_createtime` datetime DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`fu_id`),
   UNIQUE KEY `fu_name` (`fu_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 论坛主题
CREATE TABLE `forum_topic` (
  `ft_id` INT(11) NOT NULL AUTO_INCREMENT,
  `fu_id` INT(11) NOT NULL,
  `ft_category` VARCHAR(20) NOT NULL,
  `ft_title` VARCHAR(50) NOT NULL,
  `ft_comment` VARCHAR(10) DEFAULT '0',
  `ft_lastreplytime` DATETIME DEFAULT NULL,
  `ft_createtime` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ft_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 论坛主题内容
CREATE TABLE `forum_topic_content` (
  `ftc_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ft_id` INT(11) DEFAULT NULL,
  `ftc_content` longtext NOT NULL,
  `ftc_read` VARCHAR(10) DEFAULT '0',
  PRIMARY KEY (`ftc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 论坛主题回复
CREATE TABLE `forum_topic_reply` (
  `ftr_id` INT(11) NOT NULL AUTO_INCREMENT,
  `fu_id` INT(11) DEFAULT NULL,
  `ft_id` INT(11) DEFAULT NULL,
  `ftr_content` VARCHAR(150) NOT NULL,
  `ftr_agree` VARCHAR(10) DEFAULT '0',
  `ftr_oppose` VARCHAR(10) DEFAULT '0',
  `ftr_createtime` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ftr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

