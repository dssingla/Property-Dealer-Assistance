-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: propertydealer
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `username` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `notification` tinyint(1) DEFAULT NULL,
  `dark_mode` tinyint(1) DEFAULT NULL,
  `auto_backup` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `mobile` varchar(12) NOT NULL,
  `cname` varchar(100) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ctype` varchar(20) DEFAULT NULL,
  `pic` longblob,
  `acard` longblob,
  `doe` date DEFAULT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deals`
--

DROP TABLE IF EXISTS `deals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deals` (
  `deal_id` int NOT NULL AUTO_INCREMENT,
  `property_id` int NOT NULL,
  `seller_mobile` varchar(15) DEFAULT NULL,
  `seller_name` varchar(100) DEFAULT NULL,
  `buyer_mobile` varchar(15) DEFAULT NULL,
  `buyer_name` varchar(100) DEFAULT NULL,
  `deal_date` date DEFAULT NULL,
  `registry_date` date DEFAULT NULL,
  `final_amount` double DEFAULT NULL,
  `advance` double DEFAULT NULL,
  `down_payment` double DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `commission` double DEFAULT NULL,
  `deal_status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`deal_id`),
  KEY `property_id` (`property_id`),
  CONSTRAINT `deals_ibfk_1` FOREIGN KEY (`property_id`) REFERENCES `properties` (`property_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `properties` (
  `property_id` int NOT NULL AUTO_INCREMENT,
  `mobile` varchar(15) DEFAULT NULL,
  `property_type` varchar(30) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `size` varchar(50) DEFAULT NULL,
  `front_side` varchar(50) DEFAULT NULL,
  `rear_side` varchar(50) DEFAULT NULL,
  `left_side` varchar(50) DEFAULT NULL,
  `right_side` varchar(50) DEFAULT NULL,
  `direction` varchar(30) DEFAULT NULL,
  `construction_type` varchar(30) DEFAULT NULL,
  `approved_by` varchar(30) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `other_info` text,
  `image1` longblob,
  `image2` longblob,
  `property_status` varchar(20) DEFAULT 'Available',
  PRIMARY KEY (`property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-28  1:04:19
