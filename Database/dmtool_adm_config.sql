CREATE DATABASE  IF NOT EXISTS `dmtool` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `dmtool`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: dmtool
-- ------------------------------------------------------
-- Server version	5.7.9-log

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
-- Table structure for table `adm_config`
--

DROP TABLE IF EXISTS `adm_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adm_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ENV_NAME` varchar(45) DEFAULT NULL,
  `Config_Type` varchar(45) DEFAULT NULL,
  `USER_ID` varchar(45) DEFAULT NULL,
  `PASSWORD` varchar(45) DEFAULT NULL,
  `SEIBEL_SERVER` varchar(45) DEFAULT NULL,
  `ROW_ID` varchar(45) DEFAULT NULL,
  `LOG_FILE_PATH` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adm_config`
--

LOCK TABLES `adm_config` WRITE;
/*!40000 ALTER TABLE `adm_config` DISABLE KEYS */;
INSERT INTO `adm_config` VALUES (6,'QA-1',NULL,'nmn','sads','fdsf',NULL,'sadsa'),(7,'Select Environment',NULL,'user','password','seibel server','','filepath'),(8,'QA-2','Export','user id','password','seibelserver',NULL,'logfile'),(9,'QA-1','Export','nmn','sads','fdsf',NULL,'sadsa'),(10,'QA-2-Punnam','Import',NULL,NULL,NULL,NULL,NULL),(11,'QA-2-Punnam','Export','hghgjmm','jhjhmmm','jhjhjmmm','rowww','jhjhmmm'),(12,'QA-2','Export',NULL,NULL,NULL,NULL,NULL),(13,'QA-12','Export','fdsf','dfsdf','fsf','dsfs','dfsd');
/*!40000 ALTER TABLE `adm_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-16 20:22:16
