CREATE DATABASE  IF NOT EXISTS `dmtool` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `dmtool`;
-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: dmtool
-- ------------------------------------------------------
-- Server version	5.5.46-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `export_repos`
--

DROP TABLE IF EXISTS `export_repos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `export_repos` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENV_NAME` varchar(45) DEFAULT NULL,
  `USER_ID` varchar(45) DEFAULT NULL,
  `PASSWORD` varchar(45) DEFAULT NULL,
  `ODBC` varchar(45) DEFAULT NULL,
  `FILE_PATH` varchar(45) DEFAULT NULL,
  `REPO_Type` varchar(45) DEFAULT NULL,
  `LOG_FILE_PATH` varchar(45) DEFAULT NULL,
  `REPO_NAME` varchar(45) DEFAULT NULL,
  `TableDDLSync` varchar(45) DEFAULT NULL,
  `IndexDDLSync` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `export_repos`
--

LOCK TABLES `export_repos` WRITE;
/*!40000 ALTER TABLE `export_repos` DISABLE KEYS */;
INSERT INTO `export_repos` VALUES (1,'QA-1','userid','password','server','e file path','Export','logfile','row','ddl sync','index'),(2,'QA-2-Punnam',NULL,NULL,NULL,NULL,'Export',NULL,NULL,NULL,NULL),(3,'QA-2-Punnam',NULL,NULL,NULL,NULL,'DDLSync',NULL,NULL,NULL,NULL),(4,'UdateQA-1m',NULL,NULL,NULL,NULL,'Import',NULL,NULL,NULL,NULL),(5,'QA-12','dfdf','dfd','df','fsdf','Export','fdf','dfd','dfsd','dfsd');
/*!40000 ALTER TABLE `export_repos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-10 20:35:31
