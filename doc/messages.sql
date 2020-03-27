/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : community

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2020-03-27 18:47:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` tinytext,
  `contentType` int(11) DEFAULT '0' COMMENT '0:文本,1:富文本',
  `createDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_comments
-- ----------------------------
DROP TABLE IF EXISTS `post_comments`;
CREATE TABLE `post_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromId` int(11) DEFAULT NULL,
  `toId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `commentId` int(11) DEFAULT NULL,
  `replyId` int(11) DEFAULT NULL,
  `commenType` int(11) DEFAULT '0' COMMENT '0:评论帖子,1:回复评论,2:回复评论的回复',
  `content` varchar(500) DEFAULT NULL,
  `imgs` json DEFAULT NULL,
  `thumbsCount` int(11) DEFAULT '0',
  `replyCount` int(11) DEFAULT '0',
  `senDate` datetime DEFAULT NULL,
  `isRead` tinyint(1) DEFAULT '0',
  `isHot` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_likes
-- ----------------------------
DROP TABLE IF EXISTS `post_likes`;
CREATE TABLE `post_likes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `toId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `commentId` int(11) DEFAULT NULL,
  `likeType` int(11) DEFAULT '0' COMMENT '类型:0帖子,1评论',
  `senDate` datetime DEFAULT NULL,
  `isRead` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_recommends
-- ----------------------------
DROP TABLE IF EXISTS `post_recommends`;
CREATE TABLE `post_recommends` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `postId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `posType` int(11) DEFAULT NULL,
  `recommendType` int(11) DEFAULT '0' COMMENT '推荐类型(0:永久在推荐表,1:暂时24小时在推荐表)',
  `senDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_views
-- ----------------------------
DROP TABLE IF EXISTS `post_views`;
CREATE TABLE `post_views` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `senDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `questionId` int(11) DEFAULT NULL,
  `articleTitle` varchar(60) DEFAULT NULL,
  `articleImg` varchar(500) DEFAULT NULL,
  `articleHtml` text,
  `articleDelta` json DEFAULT NULL,
  `introduction` varchar(140) DEFAULT NULL,
  `content` varchar(280) DEFAULT NULL,
  `imgs` json DEFAULT NULL,
  `posType` int(11) DEFAULT '0' COMMENT '帖子类型(0:动弹,1:文章,2:问答,3:投票)',
  `link` varchar(128) DEFAULT NULL,
  `video` json DEFAULT NULL,
  `audio` json DEFAULT NULL,
  `senDate` datetime DEFAULT NULL COMMENT '发布时间',
  `thumbsCount` int(11) DEFAULT '0',
  `commentCount` int(11) DEFAULT '0',
  `viewCount` int(11) DEFAULT '0',
  `topicId` int(11) DEFAULT NULL,
  `topicTitle` varchar(255) DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL,
  `longitude` decimal(10,7) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `isRecommend` tinyint(1) DEFAULT '0',
  `isTop` tinyint(1) DEFAULT '0',
  `shell` int(11) DEFAULT '0' COMMENT '累计打赏玉帛贝数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for schools
-- ----------------------------
DROP TABLE IF EXISTS `schools`;
CREATE TABLE `schools` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sequelizemeta
-- ----------------------------
DROP TABLE IF EXISTS `sequelizemeta`;
CREATE TABLE `sequelizemeta` (
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for topic_follows
-- ----------------------------
DROP TABLE IF EXISTS `topic_follows`;
CREATE TABLE `topic_follows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `topicId` int(11) DEFAULT NULL,
  `hasFollow` tinyint(1) DEFAULT '0' COMMENT '是否关注话题',
  `score` int(11) DEFAULT '0' COMMENT '贡献积分',
  `followDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topics
-- ----------------------------
DROP TABLE IF EXISTS `topics`;
CREATE TABLE `topics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `des` varchar(255) DEFAULT NULL,
  `iconSrc` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT '书虫' COMMENT '话题用户别称',
  `topicType` int(11) DEFAULT '0' COMMENT '话题类型:(0:系统话题,1:用户自定义话题)',
  `ownerId` int(11) DEFAULT '1' COMMENT '话题创造者',
  `followCount` int(11) DEFAULT '0',
  `postCount` int(11) DEFAULT '0',
  `isTop` tinyint(1) DEFAULT '0',
  `isActivity` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for trades
-- ----------------------------
DROP TABLE IF EXISTS `trades`;
CREATE TABLE `trades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `fromId` int(11) DEFAULT NULL,
  `shell` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT '0' COMMENT '交易类型(0:变现,1:转换)',
  `state` int(11) DEFAULT NULL COMMENT '变现状态(0:申请中,1:已发放,2:已拒绝)',
  `addDate` datetime DEFAULT NULL COMMENT '申请时间',
  `senDate` datetime DEFAULT NULL COMMENT '发放时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_follows
-- ----------------------------
DROP TABLE IF EXISTS `user_follows`;
CREATE TABLE `user_follows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromId` int(11) DEFAULT NULL,
  `toId` int(11) DEFAULT NULL,
  `followDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_messages
-- ----------------------------
DROP TABLE IF EXISTS `user_messages`;
CREATE TABLE `user_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `toUserId` int(11) DEFAULT NULL,
  `messageId` int(11) DEFAULT NULL,
  `senDate` datetime DEFAULT NULL,
  `isRead` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_notices
-- ----------------------------
DROP TABLE IF EXISTS `user_notices`;
CREATE TABLE `user_notices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `toId` int(11) DEFAULT NULL,
  `fromId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `commentId` int(11) DEFAULT NULL,
  `replyId` int(11) DEFAULT NULL,
  `noticeType` int(11) DEFAULT '0' COMMENT '0:点赞帖子，1:点赞评论，2:评论，3:回复，4:关注，5:回答',
  `senDate` datetime DEFAULT NULL,
  `isRead` tinyint(1) DEFAULT '0',
  `shell` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_sys_messages
-- ----------------------------
DROP TABLE IF EXISTS `user_sys_messages`;
CREATE TABLE `user_sys_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `toId` int(11) DEFAULT NULL,
  `messageId` int(11) DEFAULT NULL,
  `senDate` datetime DEFAULT NULL,
  `isRead` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openId` varchar(255) DEFAULT NULL,
  `unionid` varchar(255) DEFAULT NULL COMMENT '用户在开放平台的唯一标识符',
  `shareId` int(11) DEFAULT NULL COMMENT '分享者ID',
  `gzhId` varchar(255) DEFAULT NULL COMMENT '公众号openId',
  `platform` varchar(255) DEFAULT NULL COMMENT '平台:微信｜QQ',
  `subscribe` tinyint(1) DEFAULT '0' COMMENT '是否订阅公众号',
  `nick` varchar(255) DEFAULT NULL COMMENT '昵称',
  `avtater` varchar(255) DEFAULT NULL COMMENT '头像',
  `feeling` int(11) DEFAULT '0' COMMENT '情感',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `constellation` varchar(255) DEFAULT NULL COMMENT '星座',
  `gender` int(11) DEFAULT '0' COMMENT '性别(0|未知,1男性,2|女性)',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `registerDate` datetime DEFAULT NULL COMMENT '注册时间',
  `isAuth` tinyint(1) DEFAULT '0' COMMENT '是否学生认证',
  `realName` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `authSrc` varchar(255) DEFAULT NULL COMMENT '认证图片地址',
  `title` varchar(255) DEFAULT NULL COMMENT '头衔',
  `isBinding` tinyint(1) DEFAULT '0' COMMENT '是否绑定个人信息',
  `signature` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `school` varchar(255) DEFAULT NULL COMMENT '就读学校',
  `education` int(11) DEFAULT NULL COMMENT '学历(0专科,1本科,2硕士,3博士)',
  `enrollmentYear` int(11) DEFAULT NULL COMMENT '入学年份',
  `userType` int(11) DEFAULT '0' COMMENT '用户类型(0普通用户1系统用户2虚拟用户)',
  `grade` int(11) DEFAULT '0' COMMENT '用户等级(0普通1优秀2高级3VIP)',
  `sysMsgCount` int(11) DEFAULT '0' COMMENT '未读系统消息',
  `noticeCount` int(11) DEFAULT '0' COMMENT '未读用户消息',
  `followNum` int(11) DEFAULT '0' COMMENT '关注',
  `fansNum` int(11) DEFAULT '0' COMMENT '粉丝数',
  `thumbsNum` int(11) DEFAULT '0' COMMENT '获赞数',
  `drill` int(11) DEFAULT '100' COMMENT '玉帛钻',
  `shell` int(11) DEFAULT '0' COMMENT '玉帛贝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openId` (`openId`),
  UNIQUE KEY `unionid` (`unionid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
