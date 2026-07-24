-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: aspmsnew
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `bill_no` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_amt` decimal(12,2) NOT NULL,
  `mode_of_payment` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`bill_no`),
  KEY `fk_bill_user` (`uid`),
  CONSTRAINT `fk_bill_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_bill_totalamt` CHECK ((`total_amt` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `cat_id` int NOT NULL AUTO_INCREMENT,
  `cat_name` varchar(100) NOT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `uq_category_name` (`cat_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `comp_id` int NOT NULL AUTO_INCREMENT,
  `comp_name` varchar(150) NOT NULL,
  PRIMARY KEY (`comp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `company_part`
--

DROP TABLE IF EXISTS `company_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_part` (
  `cpid` int NOT NULL AUTO_INCREMENT,
  `comp_id` int NOT NULL,
  `supplier_id` int DEFAULT NULL,
  `part_id` int NOT NULL,
  `sell_price` decimal(10,2) NOT NULL,
  `cost_price` decimal(10,2) NOT NULL,
  `mfg_dt` datetime DEFAULT NULL,
  `qty` int DEFAULT '0',
  PRIMARY KEY (`cpid`),
  KEY `idx_companypart_part` (`part_id`),
  KEY `idx_companypart_comp` (`comp_id`),
  KEY `fk_companypart_supplier` (`supplier_id`),
  CONSTRAINT `fk_companypart_company` FOREIGN KEY (`comp_id`) REFERENCES `company` (`comp_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_companypart_part` FOREIGN KEY (`part_id`) REFERENCES `parts` (`part_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_companypart_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `supplier_profile` (`supplier_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_companypart_costprice` CHECK ((`cost_price` >= 0)),
  CONSTRAINT `chk_companypart_qty` CHECK ((`qty` >= 0)),
  CONSTRAINT `chk_companypart_sellprice` CHECK ((`sell_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enquiry`
--

DROP TABLE IF EXISTS `enquiry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enquiry` (
  `enq_no` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `cpid` int NOT NULL,
  `qty` int NOT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  PRIMARY KEY (`enq_no`),
  KEY `fk_enquiry_companypart` (`cpid`),
  KEY `idx_enquiry_uid` (`uid`),
  CONSTRAINT `fk_enquiry_companypart` FOREIGN KEY (`cpid`) REFERENCES `company_part` (`cpid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_enquiry_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_enquiry_qty` CHECK ((`qty` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `inv_no` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `enq_no` int DEFAULT NULL,
  `cpid` int NOT NULL,
  `qty` int NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `bill_no` int NOT NULL,
  PRIMARY KEY (`inv_no`),
  KEY `fk_invoice_enquiry` (`enq_no`),
  KEY `fk_invoice_companypart` (`cpid`),
  KEY `idx_invoice_uid` (`uid`),
  KEY `idx_invoice_billno` (`bill_no`),
  CONSTRAINT `fk_invoice_bill` FOREIGN KEY (`bill_no`) REFERENCES `bill` (`bill_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_companypart` FOREIGN KEY (`cpid`) REFERENCES `company_part` (`cpid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_enquiry` FOREIGN KEY (`enq_no`) REFERENCES `enquiry` (`enq_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_invoice_amount` CHECK ((`amount` >= 0)),
  CONSTRAINT `chk_invoice_qty` CHECK ((`qty` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parts`
--

DROP TABLE IF EXISTS `parts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parts` (
  `part_id` int NOT NULL AUTO_INCREMENT,
  `subcat_id` int NOT NULL,
  `description` text,
  PRIMARY KEY (`part_id`),
  KEY `idx_parts_subcat` (`subcat_id`),
  CONSTRAINT `fk_parts_subcategory` FOREIGN KEY (`subcat_id`) REFERENCES `sub_category` (`subcat_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `rid` int NOT NULL AUTO_INCREMENT,
  `rname` varchar(50) NOT NULL,
  PRIMARY KEY (`rid`),
  UNIQUE KEY `uq_role_rname` (`rname`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_category`
--

DROP TABLE IF EXISTS `sub_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sub_category` (
  `subcat_id` int NOT NULL AUTO_INCREMENT,
  `cat_id` int NOT NULL,
  `subcat_name` varchar(100) NOT NULL,
  PRIMARY KEY (`subcat_id`),
  KEY `fk_subcategory_category` (`cat_id`),
  CONSTRAINT `fk_subcategory_category` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `supplier_profile`
--

DROP TABLE IF EXISTS `supplier_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_profile` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `gstin` varchar(15) NOT NULL,
  `verified_status` tinyint(1) DEFAULT '0',
  `contact_phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `gstin` (`gstin`),
  CONSTRAINT `fk_supplier_user` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `rid` int NOT NULL,
  `uname` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `address` text,
  PRIMARY KEY (`uid`),
  KEY `fk_user_role` (`rid`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`rid`) REFERENCES `role` (`rid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-23 23:13:25
