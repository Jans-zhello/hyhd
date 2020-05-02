/*
Navicat MySQL Data Transfer

Source Server         : zzz
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : hyhd

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2018-01-07 09:48:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `huifu`
-- ----------------------------
DROP TABLE IF EXISTS `huifu`;
CREATE TABLE `huifu` (
  `hid` int(11) NOT NULL AUTO_INCREMENT,
  `txt` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `sendtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userid` int(11) NOT NULL,
  `titleid` int(11) NOT NULL,
  PRIMARY KEY (`hid`),
  KEY `userid` (`userid`),
  KEY `titleid` (`titleid`),
  CONSTRAINT `huifu_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`),
  CONSTRAINT `huifu_ibfk_2` FOREIGN KEY (`titleid`) REFERENCES `titles` (`titleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of huifu
-- ----------------------------

-- ----------------------------
-- Table structure for `titles`
-- ----------------------------
DROP TABLE IF EXISTS `titles`;
CREATE TABLE `titles` (
  `titleid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `titletxt` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `sendcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`titleid`),
  KEY `userid` (`userid`),
  CONSTRAINT `titles_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of titles
-- ----------------------------
INSERT INTO `titles` VALUES ('10', '3', '123', 'SEND_TEXT', '2018-01-06 21:42:11');
INSERT INTO `titles` VALUES ('11', '3', '1515246145396R86R152.amr', 'SEND_AMR', '2018-01-06 21:42:26');
INSERT INTO `titles` VALUES ('12', '4', '1515246422852R451R666.amr', 'SEND_AMR', '2018-01-06 21:47:03');
INSERT INTO `titles` VALUES ('13', '4', 'asdfasdfasdf', 'SEND_TEXT', '2018-01-06 21:47:07');
INSERT INTO `titles` VALUES ('14', '4', '¹11111111', 'SEND_TEXT', '2018-01-06 21:47:18');
INSERT INTO `titles` VALUES ('15', '4', '1515246518086R816R827.amr', 'SEND_AMR', '2018-01-06 21:48:38');
INSERT INTO `titles` VALUES ('16', '4', '123', 'SEND_TEXT', '2018-01-06 23:12:54');
INSERT INTO `titles` VALUES ('17', '4', 'hehe', 'SEND_TEXT', '2018-01-06 23:52:02');
INSERT INTO `titles` VALUES ('18', '4', '1515253931998R348R594.amr', 'SEND_AMR', '2018-01-06 23:52:12');
INSERT INTO `titles` VALUES ('19', '4', '1515254858523R469R755.amr', 'SEND_AMR', '2018-01-07 00:07:38');

-- ----------------------------
-- Table structure for `userinfos`
-- ----------------------------
DROP TABLE IF EXISTS `userinfos`;
CREATE TABLE `userinfos` (
  `userid` int(11) NOT NULL,
  `uname` varchar(100) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  CONSTRAINT `userinfos_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfos
-- ----------------------------
INSERT INTO `userinfos` VALUES ('3', 'zzz', '男', 'henan', '16678498732');
INSERT INTO `userinfos` VALUES ('4', 'tiny', '女', '123', '123');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `ukey` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('3', '123', '1', '1515245717159R43.45498199155819R170.24769312756715');
INSERT INTO `users` VALUES ('4', '123', '1', '1515245891928R63.79814821447945R995.4226659778054');
