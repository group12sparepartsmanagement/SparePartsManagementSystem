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
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (1,4,'2026-07-20 10:00:00',450.00,'UPI'),(2,5,'2026-07-21 11:30:00',4500.00,'Credit Card'),(3,4,'2026-07-22 14:00:00',3200.00,'Cash on Delivery');
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (5,'Body Parts'),(3,'Brakes'),(2,'Electrical'),(1,'Engine Components'),(4,'Suspension');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'Bosch'),(2,'Denso'),(3,'NGK'),(4,'Mahindra'),(5,'Tata Motors'),(6,'Amara Raja');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `company_part`
--

LOCK TABLES `company_part` WRITE;
/*!40000 ALTER TABLE `company_part` DISABLE KEYS */;
INSERT INTO `company_part` VALUES (1,1,1,1,450.00,300.00,'2025-10-01 00:00:00',50),(2,1,1,6,2500.00,2000.00,'2025-11-15 00:00:00',30),(3,6,2,4,4500.00,3800.00,'2026-01-10 00:00:00',20),(4,2,2,7,3200.00,2600.00,'2025-12-05 00:00:00',15);
/*!40000 ALTER TABLE `company_part` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `enquiry`
--

LOCK TABLES `enquiry` WRITE;
/*!40000 ALTER TABLE `enquiry` DISABLE KEYS */;
INSERT INTO `enquiry` VALUES (1,4,1,1,'2026-07-19 09:00:00','Need an oil filter replacement urgently.'),(2,5,3,1,'2026-07-20 10:00:00','Checking compatibility for SUV.'),(3,4,4,1,'2026-07-21 12:00:00','Enquiring about warranty on shock absorbers.');
/*!40000 ALTER TABLE `enquiry` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,4,'2026-07-20 10:05:00',1,1,1,450.00,1),(2,5,'2026-07-21 11:35:00',2,3,1,4500.00,2),(3,4,'2026-07-22 14:05:00',3,4,1,3200.00,3);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `parts`
--

LOCK TABLES `parts` WRITE;
/*!40000 ALTER TABLE `parts` DISABLE KEYS */;
INSERT INTO `parts` VALUES (1,1,'Oil Filter for 1.2L Engine'),(2,1,'Air Filter Standard Performance'),(3,2,'Timing Belt Standard'),(4,3,'12V 65Ah Car Battery'),(5,4,'LED Headlight Bulb H4'),(6,5,'Ceramic Brake Pads Front Set'),(7,6,'Rear Shock Absorber Premium');
/*!40000 ALTER TABLE `parts` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Admin'),(3,'Customer'),(4,'Staff'),(2,'Supplier');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `sub_category`
--

LOCK TABLES `sub_category` WRITE;
/*!40000 ALTER TABLE `sub_category` DISABLE KEYS */;
INSERT INTO `sub_category` VALUES (1,1,'Filters'),(2,1,'Belts'),(3,2,'Batteries'),(4,2,'Lighting'),(5,3,'Brake Pads'),(6,4,'Shock Absorbers');
/*!40000 ALTER TABLE `sub_category` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `supplier_profile`
--

LOCK TABLES `supplier_profile` WRITE;
/*!40000 ALTER TABLE `supplier_profile` DISABLE KEYS */;
INSERT INTO `supplier_profile` VALUES (1,2,'27ABCDE1234F1Z5',1,'9876543210'),(2,3,'27QWERT9876A1Z9',1,'9876543211');
/*!40000 ALTER TABLE `supplier_profile` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,'System Admin','hashed_pw_1','Admin HQ'),(2,2,'AutoParts Hub Pune','hashed_pw_2','Kondhwa, Pune'),(3,2,'Latur Spares','hashed_pw_3','Latur'),(4,3,'Mohammad Anzar','hashed_pw_4','Kondhwa, Pune'),(5,3,'Zaid','hashed_pw_5','Pune'),(6,3,'Harō','hashed_pw_6','Avallon'),(7,3,'Aiko','hashed_pw_7','Avallon'),(8,4,'Staff Member 1','hashed_pw_8','Office Branch 1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-23 23:14:36
