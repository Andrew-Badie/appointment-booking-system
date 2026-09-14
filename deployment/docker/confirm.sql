-- MySQL dump 10.18  Distrib 10.3.27-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: confirm_LBS
-- ------------------------------------------------------
-- Server version	10.3.27-MariaDB-0+deb10u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `confirm_LBS`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `confirm_LBS` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `confirm_LBS`;

--
-- Table structure for table `Appointment_Book`
--

DROP TABLE IF EXISTS `Appointment_Book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Appointment_Book` (
  `username` varchar(30) DEFAULT NULL,
  `code` varchar(25) NOT NULL,
  `date1` date NOT NULL,
  PRIMARY KEY (`code`,`date1`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Appointment_Book`
--

LOCK TABLES `Appointment_Book` WRITE;
/*!40000 ALTER TABLE `Appointment_Book` DISABLE KEYS */;
/*!40000 ALTER TABLE `Appointment_Book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_APPOINTMENT_Confirm`
--

DROP TABLE IF EXISTS `USER_APPOINTMENT_Confirm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_APPOINTMENT_Confirm` (
  `username` varchar(30) NOT NULL,
  `code` varchar(25) NOT NULL,
  `date1` date DEFAULT NULL,
  PRIMARY KEY (`code`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_APPOINTMENT_Confirm`
--

LOCK TABLES `USER_APPOINTMENT_Confirm` WRITE;
/*!40000 ALTER TABLE `USER_APPOINTMENT_Confirm` DISABLE KEYS */;
INSERT INTO `USER_APPOINTMENT_Confirm` VALUES ('John','12243124','2019-06-01');
/*!40000 ALTER TABLE `USER_APPOINTMENT_Confirm` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-03 20:11:46
