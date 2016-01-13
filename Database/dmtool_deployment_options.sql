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
-- Table structure for table `deployment_options`
--

DROP TABLE IF EXISTS `deployment_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deployment_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  `command` varchar(45) DEFAULT NULL,
  `CREATED_TIME` datetime DEFAULT NULL,
  `UPDATED_TIME` datetime DEFAULT NULL,
  `CREATED_USER` int(11) DEFAULT NULL,
  `UPDATED_USER` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deployment_options`
--

LOCK TABLES `deployment_options` WRITE;
/*!40000 ALTER TABLE `deployment_options` DISABLE KEYS */;
INSERT INTO `deployment_options` VALUES (1,'StopServer','Stop Server','Option','sc $HOST_NAME Stop $SERVICE_NAME',NULL,NULL,NULL,NULL),(2,'CopySRFBS','Copy SRF/BS','Option',NULL,NULL,NULL,NULL,NULL),(3,'CopyWebTemplate','Copy Web Template','Option',NULL,NULL,NULL,NULL,NULL),(4,'CopyOtherFiles','Copy other Files (Specified in package)','Option',NULL,NULL,NULL,NULL,NULL),(5,'ImportRespository','Import Respository','Option',NULL,NULL,NULL,NULL,NULL),(6,'RenameRespository','Rename Respository','Option',NULL,NULL,NULL,NULL,NULL),(7,'ApplySchemaChanges','Apply Schema Changes','Option',NULL,NULL,NULL,NULL,NULL),(8,'StartServers','Start Servers','Option',NULL,NULL,NULL,NULL,NULL),(9,'ImportADM','Import  ADM','Option',NULL,NULL,NULL,NULL,NULL),(10,'BuildNow','Build Now','package',NULL,NULL,NULL,NULL,NULL),(11,'ScheduleDeploy','Schedule Deploy','package',NULL,NULL,NULL,NULL,NULL),(12,'MigrateSRFRepositoryNow','Migrate SRF/Repository Now','package',NULL,NULL,NULL,NULL,NULL),(13,'ADMExportImport','ADM Export/Import','package',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `deployment_options` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-13 10:49:34
