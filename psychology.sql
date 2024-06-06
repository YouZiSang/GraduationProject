-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: psychology
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` varchar(20) NOT NULL,
  `create_time` timestamp NOT NULL,
  `update_time` timestamp NOT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,37819308,'e9f5c5240c0bb39488e6dbfbdb1517e0','USER','2024-04-23 08:32:41','2024-04-23 08:32:41',0),(2,9927680,'e9f5c5240c0bb39488e6dbfbdb1517e0','CONSULTANT','2024-04-23 10:41:04','2024-04-23 10:41:04',0),(3,42021900,'e9f5c5240c0bb39488e6dbfbdb1517e0','USER','2024-04-23 10:41:08','2024-04-23 10:41:08',0),(4,974306355,'e9f5c5240c0bb39488e6dbfbdb1517e0','ADMIN','2024-04-23 10:42:11','2024-04-23 10:42:11',0),(5,206931382,'e9f5c5240c0bb39488e6dbfbdb1517e0','USER','2024-04-29 09:45:00','2024-04-29 09:45:00',0),(6,456576683,'e9f5c5240c0bb39488e6dbfbdb1517e0','CONSULTANT','2024-04-30 09:42:00','2024-04-30 09:42:00',0),(7,54384624,'e9f5c5240c0bb39488e6dbfbdb1517e0','ADMIN','2024-04-23 10:42:11','2024-04-23 10:42:11',0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int DEFAULT NULL,
  `question_id` int DEFAULT NULL,
  `answer_option` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
INSERT INTO `answers` VALUES (41,37819308,1,'B'),(42,37819308,2,'D'),(43,37819308,3,'D'),(44,37819308,4,'C'),(45,37819308,5,'D'),(46,37819308,6,'B'),(47,37819308,7,'B'),(48,37819308,8,'C'),(49,37819308,9,'C'),(50,37819308,10,'B'),(51,37819308,11,'D'),(52,37819308,12,'B'),(53,37819308,13,'C'),(54,37819308,14,'D'),(55,37819308,15,'C'),(56,37819308,16,'C'),(57,37819308,17,'C'),(58,37819308,18,'B'),(59,37819308,19,'C'),(60,37819308,20,'A'),(61,206931382,1,'C'),(62,206931382,2,'B'),(63,206931382,3,'C'),(64,206931382,4,'C'),(65,206931382,5,'B'),(66,206931382,6,'D'),(67,206931382,7,'C'),(68,206931382,8,'D'),(69,206931382,9,'B'),(70,206931382,10,'C'),(71,206931382,11,'D'),(72,206931382,12,'D'),(73,206931382,13,'B'),(74,206931382,14,'C'),(75,206931382,15,'D'),(76,206931382,16,'C'),(77,206931382,17,'B'),(78,206931382,18,'D'),(79,206931382,19,'C'),(80,206931382,20,'B');
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment_table`
--

DROP TABLE IF EXISTS `appointment_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int NOT NULL,
  `appointment_date` date NOT NULL,
  `uid` int NOT NULL,
  `type` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_table`
--

LOCK TABLES `appointment_table` WRITE;
/*!40000 ALTER TABLE `appointment_table` DISABLE KEYS */;
INSERT INTO `appointment_table` VALUES (1,9927680,'2024-05-16',37819308,'6','已评价',0),(2,9927680,'2024-05-17',37819308,'2','预约成功',0);
/*!40000 ALTER TABLE `appointment_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment_type`
--

DROP TABLE IF EXISTS `appointment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_type`
--

LOCK TABLES `appointment_type` WRITE;
/*!40000 ALTER TABLE `appointment_type` DISABLE KEYS */;
INSERT INTO `appointment_type` VALUES (1,'情绪管理'),(2,'个人成长'),(3,'人际关系'),(4,'儿童与青少年'),(5,'心理健康'),(6,'生活适应'),(7,'其他');
/*!40000 ALTER TABLE `appointment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ISBN` varchar(20) NOT NULL,
  `title` varchar(50) NOT NULL,
  `image` varchar(255) NOT NULL,
  `author` varchar(30) NOT NULL,
  `publisher` varchar(30) NOT NULL,
  `number` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `old_price` decimal(10,2) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `sales` int NOT NULL DEFAULT '0',
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ISBN_UNIQUE` (`ISBN`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'9787514512380','一旦能放声嘲笑自己，你就自由了','http://sdeumqhuw.hn-bkt.clouddn.com/bcd1eeddd8cf494793e60d6a40ac168b-tb_image_share_1714461776117.jpg?e=1715675701534&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:zfbFsRTyVMhNjM25oM_68plGpG8=','[美]梅丽莎·达尔','中国致公出版社',197,5,108.00,68.90,2,0),(2,'9787516925485','当你开始爱自己，全世界都会来爱你','http://sdeumqhuw.hn-bkt.clouddn.com/4c6f95d3d2d34a5b98fd72826dbcc770-tb_image_share_1714462051777.jpg?e=1715675719858&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:6LamcF5Dyh5ne7ZDQXb1bVKog-k=','周梵','华龄出版社',178,3,49.80,32.80,0,0),(3,'9787516824344','了不起的我','http://sdeumqhuw.hn-bkt.clouddn.com/05c1f3ef59c9429ba1756dff3cc883a7-tb_image_share_1714462386694.jpg?e=1715675731141&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:czBm8gKF0-u5yjZr9NBRTRKFjt0=','陈海贤','台海出版社',200,3,69.00,34.80,0,0),(4,'9787115371775','心理学与生活','http://sdeumqhuw.hn-bkt.clouddn.com/28d7d396607b4f498d6aa0b37167eec2-s28338983.jpg?e=1715675774688&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:vmiBdU2Xr6pEwBM0kOBW6r0pzNc=','[美]理查德·格里格','人民邮电出版社',240,1,368.00,99.80,0,0),(5,'9787516824345','124','http://sbkhe2o39.hn-bkt.clouddn.com/dab3f3c347554d17bf5325b89184dfdc-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1715025072437&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:qUrKsgsD-uC2L0-lhr3joKndDCc=','收到','打算',12,5,41.00,412.00,0,1),(6,'9787516824312','343','http://sbkhe2o39.hn-bkt.clouddn.com/7041aa8b43b144c0b52175c3598dda92-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1715025174688&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:W7fM_OZ_NipBJyDKohw36j6nTQQ=','DSA','SA',12,6,41.00,12.00,0,1);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_evaluation`
--

DROP TABLE IF EXISTS `books_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books_evaluation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(19) DEFAULT NULL,
  `goods_id` int DEFAULT NULL,
  `eva_id` bigint DEFAULT NULL,
  `content` text,
  `media` text,
  `description` varchar(4) DEFAULT NULL,
  `logistics` varchar(4) DEFAULT NULL,
  `attitude` varchar(4) DEFAULT NULL,
  `created_time` varchar(20) DEFAULT NULL,
  `updated_time` varchar(20) DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_evaluation`
--

LOCK TABLES `books_evaluation` WRITE;
/*!40000 ALTER TABLE `books_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `books_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_manage`
--

DROP TABLE IF EXISTS `books_manage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books_manage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_aid` int NOT NULL,
  `created_time` varchar(20) DEFAULT NULL,
  `updated_aid` int NOT NULL,
  `updated_time` varchar(20) DEFAULT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_manage`
--

LOCK TABLES `books_manage` WRITE;
/*!40000 ALTER TABLE `books_manage` DISABLE KEYS */;
INSERT INTO `books_manage` VALUES (1,974306355,'2024-04-30 15:26:33',974306355,'2024-05-14 16:34:58',1),(2,974306355,'2024-04-30 15:31:25',974306355,'2024-05-14 16:35:16',2),(3,974306355,'2024-04-30 15:35:37',974306355,'2024-05-14 16:35:27',3),(4,974306355,'2024-05-07 03:06:44',974306355,'2024-05-14 16:36:11',4),(5,974306355,'2024-05-07 03:52:51',974306355,'2024-05-07 04:01:28',6);
/*!40000 ALTER TABLE `books_manage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_type`
--

DROP TABLE IF EXISTS `books_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_type`
--

LOCK TABLES `books_type` WRITE;
/*!40000 ALTER TABLE `books_type` DISABLE KEYS */;
INSERT INTO `books_type` VALUES (1,'心理学基础类'),(2,'情绪管理类'),(3,'心理健康与自我帮助类'),(4,'性教育与性心理健康类'),(5,'社交技能与人际关系类'),(6,'励志与成长文学');
/*!40000 ALTER TABLE `books_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_appointment_set`
--

DROP TABLE IF EXISTS `c_appointment_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `c_appointment_set` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `appointment_date` varchar(20) DEFAULT NULL,
  `number` int DEFAULT NULL,
  `reservations` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_appointment_set`
--

LOCK TABLES `c_appointment_set` WRITE;
/*!40000 ALTER TABLE `c_appointment_set` DISABLE KEYS */;
INSERT INTO `c_appointment_set` VALUES (1,9927680,'2024-05-01 00:00:00',0,0),(2,9927680,'2024-05-01 00:00:00',12,0),(3,9927680,'2024-05-02 00:00:00',13,0),(4,9927680,'2024-05-03 00:00:00',15,0),(5,9927680,'2024-05-18 00:00:00',0,0),(6,9927680,'2024-05-18 00:00:00',0,0),(7,9927680,'2024-05-17 00:00:00',0,0),(8,9927680,'2024-05-18 00:00:00',0,0),(9,9927680,'2024-05-15 00:00:00',10,0),(10,9927680,'2024-05-16 00:00:00',11,1),(11,9927680,'2024-05-19 00:00:00',0,0),(12,9927680,'2024-05-21 00:00:00',0,0),(13,9927680,'2024-05-17 00:00:00',121,0);
/*!40000 ALTER TABLE `c_appointment_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carousel`
--

DROP TABLE IF EXISTS `carousel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carousel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_image` varchar(255) DEFAULT NULL,
  `second_image` varchar(255) DEFAULT NULL,
  `third_image` varchar(255) DEFAULT NULL,
  `fourth_image` varchar(255) DEFAULT NULL,
  `aid` int DEFAULT NULL,
  `created_time` varchar(20) DEFAULT NULL,
  `updated_time` varchar(20) DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carousel`
--

LOCK TABLES `carousel` WRITE;
/*!40000 ALTER TABLE `carousel` DISABLE KEYS */;
INSERT INTO `carousel` VALUES (1,'http://sbkhe2o39.hn-bkt.clouddn.com/7ed855a66e704ce5b2751c16388f792a-780.jpg?e=1713620758362&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:dMuvGqP9i9161zLsTfzLvXexj5w=','http://sbkhe2o39.hn-bkt.clouddn.com/7825228004394b0699ec257b99ce4402-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713552031981&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:Yc4rRTXWs5VPD7-omIOpsJLJj1I=','http://sbkhe2o39.hn-bkt.clouddn.com/2073a6713d574d8b995db6a1c7d643bc-5058.png_860.png?e=1713554436064&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:4s3FF7rjkxbSxtRRgipfVr3oWag=','http://sbkhe2o39.hn-bkt.clouddn.com/52598827452749d28d4598846d68b775-8f90-knvsnuh3426644.jpg?e=1712753535038&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:P9Yhq5Mt4uwac-nhplRfGN0AQjA=',123456,NULL,'2024-04-20 21:45:54',0);
/*!40000 ALTER TABLE `carousel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collection_book`
--

DROP TABLE IF EXISTS `collection_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection_book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `collectors_id` int NOT NULL,
  `image` varchar(255) NOT NULL,
  `title` varchar(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `ISBN` varchar(20) NOT NULL,
  `role` varchar(11) NOT NULL,
  `created_time` timestamp NOT NULL,
  `updated_time` timestamp NOT NULL,
  `author` varchar(30) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection_book`
--

LOCK TABLES `collection_book` WRITE;
/*!40000 ALTER TABLE `collection_book` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `landlord_id` int NOT NULL,
  `comment_id` int DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `content` longtext,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `message_id` int DEFAULT NULL,
  `reply_id` int DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `comment_count` int DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,1,37819308,'http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=','哟西桑','<p>666</p>','2024-05-11 13:29:39',9927680,NULL,'用户',1,1,0),(2,1,206931382,'http://sbkhe2o39.hn-bkt.clouddn.com/aa4a9bc47a0247ac893bb76dc7a7d05c-%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.png?e=1714384636629&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:1s9hIIoLwxLz_G-RT0FHGVdeCHI=','\"新青年\"','<p>555</p>','2024-05-14 11:31:58',37819308,1,'用户',0,0,1);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultants_info`
--

DROP TABLE IF EXISTS `consultants_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultants_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `avatarUrl` varchar(255) DEFAULT NULL,
  `sex` varchar(4) NOT NULL,
  `age` int NOT NULL,
  `certificate` varchar(50) NOT NULL,
  `biography` varchar(200) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` bigint NOT NULL,
  `delivery_add` varchar(50) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultants_info`
--

LOCK TABLES `consultants_info` WRITE;
/*!40000 ALTER TABLE `consultants_info` DISABLE KEYS */;
INSERT INTO `consultants_info` VALUES (1,9927680,'黄某福','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','男',24,'国家二三级心理咨询师证书','遇到心理问题就找我，统统帮你解决','2035854804@qq.com',15978346673,'南宁学院',0),(2,456576683,'陆某东',NULL,'女',55,'国家二三级心理咨询师证书','解决心理问题，带你享受人生','543676553@qq.com',19674574353,'龙庭路8号',0);
/*!40000 ALTER TABLE `consultants_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultation_evaluation`
--

DROP TABLE IF EXISTS `consultation_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultation_evaluation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eva_id` int NOT NULL,
  `consultation_date` varchar(20) DEFAULT NULL,
  `content` text NOT NULL,
  `media` text,
  `attitude` varchar(4) NOT NULL,
  `professional` varchar(4) NOT NULL,
  `reliable` varchar(4) NOT NULL,
  `created_time` varchar(20) NOT NULL,
  `cid` int NOT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultation_evaluation`
--

LOCK TABLES `consultation_evaluation` WRITE;
/*!40000 ALTER TABLE `consultation_evaluation` DISABLE KEYS */;
INSERT INTO `consultation_evaluation` VALUES (1,37819308,'2024-05-16','咨询师太好了',NULL,'4.5','5','5','2024-05-13 14:33:01',9927680,0),(2,37819308,'2024-05-16','咨询师太好了',NULL,'3.5','5','5','2024-05-13 14:38:35',9927680,0),(3,37819308,'2024-05-16','老师太好了',NULL,'5','5','5','2024-05-13 14:40:50',9927680,0),(4,37819308,'2024-05-16','咨询师太好了',NULL,'4.5','5','5','2024-05-13 15:25:48',9927680,0);
/*!40000 ALTER TABLE `consultation_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultation_table`
--

DROP TABLE IF EXISTS `consultation_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultation_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `consultation_date` varchar(20) DEFAULT NULL,
  `start_time` varchar(20) DEFAULT NULL,
  `end_time` varchar(20) DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `description` text,
  `cid` int NOT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultation_table`
--

LOCK TABLES `consultation_table` WRITE;
/*!40000 ALTER TABLE `consultation_table` DISABLE KEYS */;
INSERT INTO `consultation_table` VALUES (1,37819308,'生活适应','2024-05-16','13:33','15:25','已评价','http://sdeumqhuw.hn-bkt.clouddn.com/3d4c0803f8574d10a7bb59f6b89fe96c-%E5%BF%83%E7%90%86%E5%81%A5%E5%BA%B7.docx?e=1715582434486&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:ivcOosaUhJQcltyfcw--Ym3jIE0=',9927680,0),(3,37819308,'个人成长','2024-05-17','19:28',NULL,'正在咨询',NULL,9927680,0);
/*!40000 ALTER TABLE `consultation_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_chat`
--

DROP TABLE IF EXISTS `group_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_chat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `avatarUrl` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `time` timestamp NOT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_chat`
--

LOCK TABLES `group_chat` WRITE;
/*!40000 ALTER TABLE `group_chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `landlord_messages`
--

DROP TABLE IF EXISTS `landlord_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `landlord_messages` (
  `landlord_id` int NOT NULL AUTO_INCREMENT,
  `message_id` int DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `content` longtext,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `role` varchar(10) DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `comment_count` int DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`landlord_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `landlord_messages`
--

LOCK TABLES `landlord_messages` WRITE;
/*!40000 ALTER TABLE `landlord_messages` DISABLE KEYS */;
INSERT INTO `landlord_messages` VALUES (1,9927680,'http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','黄某福','<div>亲爱的青少年朋友们，\n面对生活中的压力，首先要知道，你并不孤单。作为心理咨询师，我想提醒你，学会管理压力是成长过程中的重要一课。以下是一些实用的建议：<span style=\"font-size: 16px;\"><br/></span></div><ol><li><strong>自我认知</strong>：了解压力的来源，是学业、人际关系还是家庭期望？</li><li><strong>时间管理</strong>：合理规划你的时间，为学习和休闲留出空间。</li><li><strong>身体活动</strong>：定期锻炼，如散步、跑步或瑜伽，可以显著减轻压力。</li><li><strong>社交互动</strong>：与朋友和家人交流你的感受，社交支持是缓解压力的关键。</li><li><strong>放松技巧</strong>：尝试深呼吸、冥想或正念练习，帮助你放松身心。</li><li><strong>专业帮助</strong>：如果压力变得难以管理，不要犹豫寻求专业的心理咨询。 记住，每个人都有面对压力的时候，寻求帮助是一种勇气，也是自我照顾的重要部分。你的感受很重要，你值得拥有一个轻松愉快的生活。 真诚地， 一位关心你的心理咨询师😇</li></ol>','2024-04-30 10:11:30','咨询师',2,2,0);
/*!40000 ALTER TABLE `landlord_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `aid` int DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  `content` text NOT NULL,
  `created_time` varchar(20) DEFAULT NULL,
  `updated_time` varchar(20) DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,NULL,'星期六上班通知','<p>今天不上班</p>','2024-04-19 21:10:18','2024-04-20 00:21:46',0),(2,NULL,'星期六上班通知','<p>今天上班</p>','2024-04-19 21:11:13','2024-04-19 21:11:13',0),(3,NULL,'你好','<p>你哦好</p><img src=\"http://localhost:8080/api/upload/1713533425926\" contenteditable=\"false\" style=\"max-width: 100%;\"/>','2024-04-19 21:30:27','2024-04-19 21:30:27',0),(4,NULL,'','<p>什么鬼啊<img src=\"http://localhost:8080/api/upload/1713535842157\" contenteditable=\"false\" style=\"max-width: 100%;\"/></p>','2024-04-19 22:10:44','2024-04-19 22:10:44',0),(5,NULL,'明天不上班','<p>明天休息</p>','2024-04-20 21:27:55','2024-04-20 21:27:55',0),(6,123456,'是','<p>是</p>','2024-04-20 21:29:15','2024-04-20 21:29:15',0);
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
  `id` int NOT NULL AUTO_INCREMENT,
  `options` varchar(10) NOT NULL,
  `option_text` varchar(100) NOT NULL,
  `question_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
INSERT INTO `options` VALUES (1,'A','从不',1),(2,'B','很少',1),(3,'C','有时',1),(4,'D','经常',1),(5,'E','总是',1),(6,'A','总是，我对我的生活有很强的控制感',2),(7,'B','大多数时候，我能够按照自己的意愿行事',2),(8,'C','有时',2),(9,'D','很少，我觉得自己常常受到外界因素的影响',2),(10,'E','很难，我感觉我的生活和决策总是失控的',2),(11,'A','从不',3),(12,'B','很少',3),(13,'C','有时',3),(14,'D','经常',3),(15,'E','总是',3),(16,'A','从不',4),(17,'B','很少',4),(18,'C','有时',4),(19,'D','经常',4),(20,'E','总是',4),(21,'A','我能够积极应对并从中恢复',5),(22,'B','我通常需要一些时间来适应，但最终能够处理',5),(23,'C','我感到有些困难，但努力寻求帮助和支持',5),(24,'D','我经常感到不知所措，需要很长时间才能适应',5),(25,'E','我很难应对，这些事件对我影响很大',5),(26,'A','从不',6),(27,'B','很少',6),(28,'C','有时',6),(29,'D','经常',6),(30,'E','总是',6),(31,'A','非常和谐',7),(32,'B','比较和谐',7),(33,'C','一般',7),(34,'D','不太和谐',7),(35,'E','非常不和谐',7),(36,'A','从不',8),(37,'B','很少',8),(38,'C','有时',8),(39,'D','经常',8),(40,'E','总是',8),(41,'A','五个以上',9),(42,'B','三到四个',9),(43,'C','一到两个',9),(44,'D','没有',9),(45,'E','我更喜欢独处',9),(46,'A','非常自信',10),(47,'B','比较自信',10),(48,'C','一般',10),(49,'D','不太自信',10),(50,'E','完全不自信',10),(51,'A','非常满意',11),(52,'B','比较满意',11),(53,'C','一般',11),(54,'D','不太满意',11),(55,'E','非常不满意',11),(56,'A','从不',12),(57,'B','很少',12),(58,'C','有时',12),(59,'D','经常',12),(60,'E','总是',12),(61,'A','非常同意',13),(62,'B','同意',13),(63,'C','中立',13),(64,'D','不同意',13),(65,'E','非常不同意',13),(66,'A','从不',14),(67,'B','很少',14),(68,'C','有时',14),(69,'D','经常',14),(70,'E','总是',14),(71,'A','总是，我总能保持积极态度',15),(72,'B','大多数时候，我能够应对',15),(73,'C','有时，这取决于挫折的严重程度',15),(74,'D','很少，我通常感到困扰',15),(75,'E','从不，我觉得自己无法应对',15),(76,'A','没有',16),(77,'B','有一个',16),(78,'C','有两个',16),(79,'D','有三个',16),(80,'E','三个以上',16),(81,'A','是，多次',17),(82,'B','是，一次',17),(83,'C','考虑过，但没有寻求',17),(84,'D','没有，但觉得可能需要',17),(85,'E','没有，也不觉得需要',17),(86,'A','非常健康',18),(87,'B','比较健康',18),(88,'C','一般',18),(89,'D','不太健康',18),(90,'E','非常不健康',18),(91,'A','从不，我对我所做的每个决定都感到满意',19),(92,'B','很少，我通常对自己的选择有信心',19),(93,'C','有时，但不是经常',19),(94,'D','经常，我有时会怀疑自己的选择',19),(95,'E','总是，我几乎总是对自己的决定感到后悔',19),(96,'A','从不',20),(97,'B','很少',20),(98,'C','有时',20),(99,'D','经常',20),(100,'E','总是',20);
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(19) DEFAULT NULL,
  `goods_id` int DEFAULT NULL,
  `goods_name` varchar(30) NOT NULL,
  `number` int DEFAULT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `created_time` timestamp NOT NULL,
  `pay_no` varchar(28) DEFAULT NULL,
  `pay_time` varchar(30) DEFAULT NULL,
  `pay_status` varchar(5) NOT NULL,
  `status` varchar(20) DEFAULT NULL,
  `buyer_id` bigint DEFAULT NULL,
  `delivery_add` varchar(50) DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'202409343613883',1,'一旦能放声嘲笑自己，你就自由了',2,136.00,'2024-04-30 07:59:53','2024043022001479230502886600','2024-04-30 16:17:41','已支付','已收货',37819308,'南宁学院',0),(2,'2024528462245605458',2,'当你开始爱自己，全世界都会来爱你',2,65.60,'2024-05-14 11:30:24',NULL,'2024-05-14 20:00:24','未支付',NULL,37819308,'南宁学院',1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_text` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'你是否有持续的忧虑或担心？','1'),(2,'你是否觉得自己能够控制自己的生活习惯和决策？','2'),(3,'你是否有难以入睡或保持睡眠的问题？','3'),(4,'你是否感到经常疲劳或没有活力？','1'),(5,'过去一年中，面对重大生活事件（如健康问题、家庭变故等），你通常如何应对？','3'),(6,'你是否有自我伤害的念头或行为？','2'),(7,'你是否觉得自己与家人的关系和谐？','4'),(8,'你是否觉得自己在学校或社交场合受到排斥或欺凌？','4'),(9,' 你是否有信任和依赖的朋友？','4'),(10,'你是否觉得自己在学习或课外活动中表现良好的自信？','2'),(11,'你是否对自己的外貌感到满意？','3'),(12,'你是否经常感到无助或绝望？','1'),(13,'你是否觉得自己有未来的目标和希望？','1'),(14,'你是否有过超出自己控制的饮酒或药物使用？','4'),(15,'你是否感到自己能够有效地应对日常生活中的小挫折？','3'),(16,'你是否在过去的一年中有重大的生活变化（如搬家、父母离异、亲人去世等）？','3'),(17,'你是否寻求过心理健康专业人士的帮助？','4'),(18,'你是否觉得自己在网络或社交媒体上有健康的使用习惯？','1'),(19,'你是否经常对自己的决定感到后悔？','2'),(20,'你是否觉得自己在处理人际关系方面有困难？','2');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions_type`
--

DROP TABLE IF EXISTS `questions_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions_type`
--

LOCK TABLES `questions_type` WRITE;
/*!40000 ALTER TABLE `questions_type` DISABLE KEYS */;
INSERT INTO `questions_type` VALUES (1,'情绪状态与感受类'),(2,'自我认知与行为类'),(3,'生活事件与适应类'),(4,'社交关系与支持类');
/*!40000 ALTER TABLE `questions_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_cart`
--

DROP TABLE IF EXISTS `shopping_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shopping_cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shopper_id` int NOT NULL,
  `goods_id` int DEFAULT NULL,
  `image` varchar(255) NOT NULL,
  `title` varchar(50) NOT NULL,
  `ISBN` varchar(20) NOT NULL,
  `author` varchar(30) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `number` int NOT NULL,
  `total_price` decimal(12,2) NOT NULL,
  `delivery_add` varchar(50) DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_cart`
--

LOCK TABLES `shopping_cart` WRITE;
/*!40000 ALTER TABLE `shopping_cart` DISABLE KEYS */;
INSERT INTO `shopping_cart` VALUES (3,37819308,1,'http://sbkhe2o39.hn-bkt.clouddn.com/c26b8a83e14d404ea0a6a095764edef6-tb_image_share_1714461776117.jpg?e=1714461996960&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:_2KfdqL6lKcSV_CWgoSAE3gb94E=','一旦能放声嘲笑自己，你就自由了','9787514512380','[美]梅丽莎·达尔',68.90,1,68.90,'南宁学院',0);
/*!40000 ALTER TABLE `shopping_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `single_chat`
--

DROP TABLE IF EXISTS `single_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `single_chat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `from_id` int NOT NULL,
  `from_name` varchar(30) NOT NULL,
  `from_avatar` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(50) DEFAULT NULL,
  `to_id` int NOT NULL,
  `to_name` varchar(30) NOT NULL,
  `to_avatar` varchar(255) DEFAULT NULL,
  `readed` int DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `single_chat`
--

LOCK TABLES `single_chat` WRITE;
/*!40000 ALTER TABLE `single_chat` DISABLE KEYS */;
INSERT INTO `single_chat` VALUES (1,9927680,'黄某福-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','你好，同学😀','2024-04-30 09:49:11','text',37819308,'用户-哟西桑','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=',1),(2,37819308,'哟西桑-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=','你好，咨询师','2024-04-30 09:49:26','text',9927680,'咨询师-黄某福','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=',1),(3,9927680,'黄某福-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','我发个文件给你吧','2024-05-13 06:21:39','text',37819308,'用户-哟西桑','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=',1),(4,9927680,'黄某福-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','http://sdeumqhuw.hn-bkt.clouddn.com/875e6d271b1943d495e4074e2e5181ca-%E5%BF%83%E7%90%86%E5%81%A5%E5%BA%B7.docx','2024-05-13 06:21:52','file',37819308,'用户-哟西桑','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=',1),(5,9927680,'黄某福-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','tongu\n\n','2024-05-14 11:29:10','text',37819308,'用户-哟西桑','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=',1),(6,37819308,'哟西桑-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=','😛','2024-05-14 11:29:21','text',9927680,'咨询师-黄某福','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=',1),(7,9927680,'黄某福-咨询师','http://sbkhe2o39.hn-bkt.clouddn.com/8b3c867a701240fdb09225b02c2b0422-01795058158d43a84a0d304f64b44d.png%401280w_1l_2o_100sh.png?e=1714382663773&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:-lqM_6RBiDKm3LDGMDzEyTZSMak=','http://sdeumqhuw.hn-bkt.clouddn.com/2b2af54ad7d34d9ea3213c02abe6b26d-8d696e2b6e64447cbf6eb3f98801362b.png','2024-05-14 11:29:50','img',37819308,'用户-哟西桑','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=',1);
/*!40000 ALTER TABLE `single_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey_scores`
--

DROP TABLE IF EXISTS `survey_scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `survey_scores` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `type` int NOT NULL,
  `type_score` varchar(5) DEFAULT NULL,
  `created_time` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey_scores`
--

LOCK TABLES `survey_scores` WRITE;
/*!40000 ALTER TABLE `survey_scores` DISABLE KEYS */;
INSERT INTO `survey_scores` VALUES (1,37819308,1,'16','2024-05-11 20:43:58'),(2,37819308,2,'20','2024-05-11 20:43:58'),(3,37819308,3,'15','2024-05-11 20:43:58'),(4,37819308,4,'13','2024-05-11 20:43:58'),(5,37819308,1,'16','2024-05-11 20:44:09'),(6,37819308,2,'20','2024-05-11 20:44:09'),(7,37819308,3,'15','2024-05-11 20:44:09'),(8,37819308,4,'13','2024-05-11 20:44:09'),(9,37819308,1,'18','2024-05-11 21:03:40'),(10,37819308,2,'20','2024-05-11 21:03:40'),(11,37819308,3,'14','2024-05-11 21:03:40'),(12,37819308,4,'15','2024-05-11 21:03:40'),(13,206931382,1,'14','2024-05-14 19:31:27'),(14,206931382,2,'16','2024-05-14 19:31:27'),(15,206931382,3,'14','2024-05-14 19:31:27'),(16,206931382,4,'16','2024-05-14 19:31:27');
/*!40000 ALTER TABLE `survey_scores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thumbs_up`
--

DROP TABLE IF EXISTS `thumbs_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thumbs_up` (
  `id` int NOT NULL AUTO_INCREMENT,
  `landlord_id` int NOT NULL,
  `thumbs_up_id` int DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `message_id` int DEFAULT NULL,
  `comment_reply_id` int DEFAULT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `landlord_id` (`landlord_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thumbs_up`
--

LOCK TABLES `thumbs_up` WRITE;
/*!40000 ALTER TABLE `thumbs_up` DISABLE KEYS */;
INSERT INTO `thumbs_up` VALUES (3,1,9927680,'2024-04-30 10:17:41',9927680,NULL,0),(4,1,37819308,'2024-04-30 10:18:20',9927680,NULL,0),(5,1,206931382,'2024-05-14 11:31:53',9927680,1,0);
/*!40000 ALTER TABLE `thumbs_up` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `avatarUrl` varchar(255) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` bigint DEFAULT NULL,
  `delivery_add` varchar(100) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,37819308,'游志','http://sbkhe2o39.hn-bkt.clouddn.com/5d3fa37120e640f880843fdc8b00f889-3215D1E3E6E53A168DAF5DCD91E86C10.png?e=1713861511789&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:yzn6RNYQyrwEVyZyG5rsyyAeOFU=','哟西桑','男',22,'783893171@qq.com',19117535404,'南宁学院',0),(2,42021900,NULL,'logo.png','落叶','',NULL,NULL,19117535404,NULL,0),(3,206931382,'房贷发放','http://sbkhe2o39.hn-bkt.clouddn.com/aa4a9bc47a0247ac893bb76dc7a7d05c-%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.png?e=1714384636629&token=QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY:1s9hIIoLwxLz_G-RT0FHGVdeCHI=','\"新青年\"','男',11,'783893171@qq.com',13321412412,'哇额为为啊我让',0);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-06 18:08:23
