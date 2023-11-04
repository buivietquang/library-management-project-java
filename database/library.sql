-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: library
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_book`
--

DROP TABLE IF EXISTS `tbl_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_book` (
  `id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `state` int NOT NULL DEFAULT '1' COMMENT '1 - dung, 2 - khong dung',
  `NXB_id` int DEFAULT NULL,
  `book_type_id` char(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `title` text CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `ISBN` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `time_XB` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT 'thời gian xuất bản',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ISBN` (`ISBN`),
  KEY `fk_book1` (`NXB_id`),
  KEY `fk_book2` (`book_type_id`),
  CONSTRAINT `fk_book1` FOREIGN KEY (`NXB_id`) REFERENCES `tbl_nxb` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_book`
--

LOCK TABLES `tbl_book` WRITE;
/*!40000 ALTER TABLE `tbl_book` DISABLE KEYS */;
INSERT INTO `tbl_book` VALUES ('AN0003',1,1,'AN','Âm nhạc các dân tộc Việt Nam','9781782808084','Hoàng Hiên','2020','2018-05-15 10:31:12.000000','2023-09-20 01:09:34.000000'),('AT0003',1,5,'AT','Món ngon 36 phố phường','9780987317209','Trần Quỳnh','2020','2018-05-15 10:33:05.000000','2023-09-07 17:12:45.000000'),('DL0002',1,1,'DL','Dải đất hình chữ S','9780733426094','Mai Trí Công','2023','2018-05-15 10:22:59.000000','2023-09-07 17:13:11.000000'),('KH0003',1,2,'KH','Gương sáng','9788493630881','Mai An','2021','2018-05-15 19:46:50.000000','2023-09-07 17:13:18.000000'),('KH0004',1,3,'KH','PT&TKPM','9783161484100','Nhóm 2','2023','2023-09-20 01:14:48.000000',NULL),('TH0002',1,5,'TH','Lập trình C++','9780955301001','Nguyễn Ngọc Huyền','2000','2018-05-15 10:35:38.000000',NULL),('TH0003',1,1,'TH','Trí tuệ nhân tạo','9788175257665','Phan Văn','2016','2018-05-15 19:37:36.000000',NULL),('TH0004',1,5,'TH','Tin học đại cương','9780130417176','Nguyễn Văn An','2023','2018-05-15 09:35:51.000000','2023-09-07 17:13:24.000000'),('TT0001',1,6,'TT','Tý Quậy','9782981558206','Thanh Hải','2006','2018-05-15 19:40:25.000000',NULL),('TT0003',1,4,'TT','Xóm nghèo','9780205028801','Đặng Văn Hào','2010','2018-05-15 19:43:41.000000',NULL),('TT0004',1,2,'TT','Tuyển tập truyện cổ tích cho bé','9788533613409','Nguyễn Kim','2018','2018-05-15 19:45:02.000000',NULL),('TT0005',1,4,'TT','Chuyến đi của Tô','9780735623873','Đặng Văn Hào','2020','2018-05-15 19:41:38.000000','2023-09-07 17:14:03.000000');
/*!40000 ALTER TABLE `tbl_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_book_type`
--

DROP TABLE IF EXISTS `tbl_book_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_book_type` (
  `id` char(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `name` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_book_type`
--

LOCK TABLES `tbl_book_type` WRITE;
/*!40000 ALTER TABLE `tbl_book_type` DISABLE KEYS */;
INSERT INTO `tbl_book_type` VALUES ('AN','Âm nhạc','2017-12-12 00:00:00.000000',NULL),('AT','Ẩm thực','2017-12-12 00:00:00.000000',NULL),('DL','Địa lý','2018-05-07 00:00:00.000000',NULL),('GK','Sách giáo khoa','2017-12-12 00:00:00.000000',NULL),('KH','Khoa học','2017-12-12 00:00:00.000000',NULL),('TH','Tin học','2018-05-07 00:00:00.000000',NULL),('TT','Truyện tranh','2018-05-15 19:39:04.000000',NULL),('VH','Văn học','2018-05-07 00:00:00.000000',NULL);
/*!40000 ALTER TABLE `tbl_book_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_borrow_card`
--

DROP TABLE IF EXISTS `tbl_borrow_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_borrow_card` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active_at` date DEFAULT NULL COMMENT 'ngay kich hoat',
  `expired_date` date DEFAULT NULL COMMENT 'ngay het han',
  `activated_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT 'ma kich hoat',
  `state` int NOT NULL DEFAULT '1' COMMENT '0 - chua kich hoat, 1 - kich hoat, 3 - blocked, 4 - het han',
  `id_user` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_card` (`id_user`),
  CONSTRAINT `fk_card` FOREIGN KEY (`id_user`) REFERENCES `tbl_user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_borrow_card`
--

LOCK TABLES `tbl_borrow_card` WRITE;
/*!40000 ALTER TABLE `tbl_borrow_card` DISABLE KEYS */;
INSERT INTO `tbl_borrow_card` VALUES (1,'2018-03-03','2019-03-03','AHFKHAK',1,1),(2,'2018-03-10','2019-03-10','FADGWED',1,2),(3,'2014-01-11','2015-01-11','GSSHSH',3,3),(4,'2015-02-03','2015-02-03','FJDLAGJA',3,3),(5,'2017-12-12','2018-12-12','FQWLJGLA',1,3),(6,'2017-10-12','2018-10-12','FALKJWJ',1,4),(7,'2018-05-05','2019-05-05','AJDFLJW',1,5);
/*!40000 ALTER TABLE `tbl_borrow_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_borrowed_books`
--

DROP TABLE IF EXISTS `tbl_borrowed_books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_borrowed_books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `state` int DEFAULT '1' COMMENT '1 - dang ky muon, 2 - da muon, 3 - tra du, 4 - tra khong du',
  `borrow_card_id` int DEFAULT NULL,
  `register_time` datetime(6) DEFAULT NULL,
  `borrowed_time` datetime(6) DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `deposits` int DEFAULT NULL,
  `id_librarian` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_borrow` (`borrow_card_id`),
  CONSTRAINT `fk_borrow` FOREIGN KEY (`borrow_card_id`) REFERENCES `tbl_borrow_card` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_borrowed_books`
--

LOCK TABLES `tbl_borrowed_books` WRITE;
/*!40000 ALTER TABLE `tbl_borrowed_books` DISABLE KEYS */;
INSERT INTO `tbl_borrowed_books` VALUES (1,4,1,'2018-04-10 00:00:00.000000','2018-04-11 00:00:00.000000','2018-05-12','2018-04-10 00:00:00.000000','2018-05-09 00:00:00.000000',0,6),(28,3,2,'2023-09-07 17:30:39.000000','2023-09-07 05:31:00.000000','2023-09-21','2023-09-07 17:30:39.000000','2023-09-07 05:31:00.000000',1000,0),(34,4,2,'2023-09-20 01:07:19.000000','2023-09-20 01:16:00.000000','2023-10-04','2023-09-20 01:07:19.000000','2023-09-20 01:16:00.000000',0,6);
/*!40000 ALTER TABLE `tbl_borrowed_books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_copy`
--

DROP TABLE IF EXISTS `tbl_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_copy` (
  `id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `state` int NOT NULL DEFAULT '1',
  `book_id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `price` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `type` int NOT NULL DEFAULT '1' COMMENT '1 - cho muon, 2 - tham khao',
  PRIMARY KEY (`id`),
  KEY `fk_copy1` (`book_id`),
  CONSTRAINT `fk_copy1` FOREIGN KEY (`book_id`) REFERENCES `tbl_book` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_copy`
--

LOCK TABLES `tbl_copy` WRITE;
/*!40000 ALTER TABLE `tbl_copy` DISABLE KEYS */;
INSERT INTO `tbl_copy` VALUES ('AN00011',1,'AN0003',0,'2018-05-15 11:00:00.000000','2023-09-07 05:29:32.000000',1),('AN00012',0,'AN0003',98000,'2018-05-15 11:06:00.000000','2023-09-20 01:09:49.000000',1),('AN00013',0,'AN0003',98000,'2018-05-15 12:06:00.000000','2023-09-07 05:43:46.000000',1),('AN00014',0,'AN0003',98000,'2018-05-15 12:13:00.000000','2018-05-16 11:21:26.000000',1),('AN00015',0,'AN0003',98000,'2018-05-15 13:00:00.000000','2023-09-14 07:42:07.000000',1),('AT00011',1,'AT0003',0,'2018-05-15 10:40:00.000000','2023-09-07 05:30:05.000000',1),('AT00012',0,'AT0003',34000,'2018-05-15 10:45:00.000000','2018-05-15 10:45:00.000000',2),('AT00013',0,'AT0003',34000,'2018-05-15 11:05:00.000000','2023-09-08 01:50:56.000000',1),('KH00031',0,'KH0003',10000,'2023-09-14 07:42:32.000000',NULL,2),('KH00032',0,'KH0003',10000,'2023-09-14 07:42:33.000000',NULL,2),('TH00011',0,'TH0004',34000,'2018-05-15 09:37:00.000000','2023-09-20 01:06:33.000000',1),('TH00012',1,'TH0004',34000,'2018-05-15 09:42:00.000000','2023-09-20 01:07:19.000000',1),('TH00021',1,'TH0002',34500,'2018-05-15 14:00:00.000000','2023-09-07 05:29:25.000000',1),('TH00022',0,'TH0002',34500,'2018-05-15 14:06:00.000000','2023-09-20 01:17:22.000000',1),('TT00011',0,'TT0001',12000,'2023-09-14 07:43:37.000000',NULL,2),('TT00012',0,'TT0001',12000,'2023-09-14 07:43:37.000000',NULL,2),('TT00013',0,'TT0001',12000,'2023-09-14 07:43:37.000000',NULL,2),('TT00041',0,'TT0004',20000,'2023-09-14 07:43:07.000000',NULL,2),('TT00042',0,'TT0004',20000,'2023-09-14 07:43:07.000000',NULL,2),('TT00043',0,'TT0004',20000,'2023-09-14 07:43:07.000000',NULL,2),('TT00044',0,'TT0004',20000,'2023-09-14 07:43:07.000000',NULL,2),('VH00011',0,NULL,29500,'2018-05-15 15:00:00.000000','2023-09-14 07:42:07.000000',1);
/*!40000 ALTER TABLE `tbl_copy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_detail_borrowed_book`
--

DROP TABLE IF EXISTS `tbl_detail_borrowed_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_detail_borrowed_book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `state` int DEFAULT '1' COMMENT '1 - dang ky muon, 2 - da muon, 3 - da tra, 4 - mat, 5 - hong',
  `borrowed_book_id` int DEFAULT NULL,
  `copy_id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `received_user_id` int DEFAULT NULL,
  `pay_date` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_detail` (`borrowed_book_id`),
  KEY `fk_detail1` (`copy_id`),
  CONSTRAINT `fk_detail` FOREIGN KEY (`borrowed_book_id`) REFERENCES `tbl_borrowed_books` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_detail1` FOREIGN KEY (`copy_id`) REFERENCES `tbl_copy` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_detail_borrowed_book`
--

LOCK TABLES `tbl_detail_borrowed_book` WRITE;
/*!40000 ALTER TABLE `tbl_detail_borrowed_book` DISABLE KEYS */;
INSERT INTO `tbl_detail_borrowed_book` VALUES (1,3,1,'TH00011',6,'2018-05-09','2018-04-10 00:00:00.000000','2018-05-09 00:00:00.000000'),(2,3,NULL,'TH00012',6,'2018-05-06','2018-04-04 00:00:00.000000','2018-05-06 00:00:00.000000'),(3,5,NULL,'VH00011',6,'2018-05-06','2018-04-04 00:00:00.000000','2018-05-06 00:00:00.000000'),(5,2,1,'AN00011',6,NULL,'2018-04-10 00:00:00.000000','2018-05-09 00:00:00.000000'),(6,3,NULL,'AN00012',6,'2018-05-06','2018-04-04 00:00:00.000000','2018-05-06 00:00:00.000000'),(7,3,NULL,'AN00013',6,'2018-05-06','2018-04-04 00:00:00.000000','2018-05-06 00:00:00.000000'),(9,3,1,'AT00011',6,'2018-05-09','2018-04-10 00:00:00.000000','2018-05-09 00:00:00.000000'),(10,3,NULL,'AT00011',6,'2018-05-06','2018-04-04 00:00:00.000000','2018-05-06 00:00:00.000000'),(40,3,28,'TH00022',0,'2023-09-07','2023-09-07 17:30:39.000000','2023-09-07 17:42:59.000000'),(49,1,34,'TH00012',NULL,NULL,'2023-09-20 01:07:19.000000',NULL),(50,3,34,'TH00022',6,'2023-09-20','2023-09-20 01:07:25.000000','2023-09-20 01:17:22.000000');
/*!40000 ALTER TABLE `tbl_detail_borrowed_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_nxb`
--

DROP TABLE IF EXISTS `tbl_nxb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_nxb` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `fax` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `email` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_nxb`
--

LOCK TABLES `tbl_nxb` WRITE;
/*!40000 ALTER TABLE `tbl_nxb` DISABLE KEYS */;
INSERT INTO `tbl_nxb` VALUES (1,'NXB Văn Hóa','046464654','nxbvanhoa@gmail.com','12 Vo Van Tan','2018-05-07 00:00:00.000000',NULL),(2,'NXB Chính trị','0436925814','nxbchinhtri@gmail.com','100 Truong Dinh','2018-03-02 00:00:00.000000',NULL),(3,'NXB Sự thật','0236420987','nxbsuthat@gmail.com','12 Hoàng Hoa Thám, Q1, TP HCM','2017-12-12 00:00:00.000000',NULL),(4,'NXB Kim Đồng','3425697564','kimdong@gmail.com','12/4 Lý Thường Kiệt, Hà Nội','2017-12-12 00:00:00.000000',NULL),(5,'NXB Giáo dục','0326564664','nxbgddt@gmail.com','1 Nguyễn Khuyến, Hà Nội','2017-12-12 00:00:00.000000',NULL),(6,'NXB Phụ Nữ','0354648784','nxbphunu@gmail.com','12 phường Bến Nghé, Q1, TP HCM','2018-05-15 19:38:47.000000',NULL);
/*!40000 ALTER TABLE `tbl_nxb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user`
--

DROP TABLE IF EXISTS `tbl_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `state` int DEFAULT '0' COMMENT '1 - chua kich hoat, 2 - kich hoat, 3 - het han',
  `gender` int DEFAULT '0',
  `email` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `phone_number` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `mssv` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `password` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL COMMENT 'password sẽ sử dụng hàm băm trước khi gửi để lưu vào csdl',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `role` int DEFAULT '1' COMMENT '1: nguoi dung\n2: nhan vien thu vien',
  `study_period` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT 'thoi gian hoc (neu la sv)',
  `id_card` int DEFAULT NULL COMMENT 'ma the muon hien tai',
  `fullname` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user` (`id_card`),
  CONSTRAINT `fk_user` FOREIGN KEY (`id_card`) REFERENCES `tbl_borrow_card` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES (1,1,0,'abc@gmail.com','0123456789','21000000','123','2018-03-02 06:26:10.000000',NULL,1,'2015-2020',1,'Nguyễn Văn A'),(2,1,0,'quang@gmail.com','0147258369','21010614','123','2018-03-02 08:10:28.000000',NULL,1,'2021-2025',2,'Bùi Việt Quang'),(3,1,1,'mongthom@gmail.com','09456123789','20141620','123','2014-01-01 00:00:00.000000',NULL,1,'2014-2019',5,'Nguyễn Mộng Thơm'),(4,1,0,'manhhung123@gmail.com','09745364159','20161234','123','2016-10-10 00:00:00.000000',NULL,1,'2016-2021',6,'Trần Mạnh Hùng'),(5,1,0,'hungphan@yahoo.com.vn','0916567099','20156666','123','2018-04-05 00:00:00.000000',NULL,1,'2015-2020',7,'Phan Văn Hùng'),(6,1,1,'admin@gmail.com','01655805411','20153805','123456','2018-11-11 00:00:00.000000','2018-11-11 00:00:00.000000',2,NULL,NULL,'admin');
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-07 12:40:06
