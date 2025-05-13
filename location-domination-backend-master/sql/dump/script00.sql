-- MySQL dump 10.13  Distrib 8.0.30, for macos11.6 (x86_64)
--
-- Host: localhost    Database: lodo
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(4) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `street` varchar(50) DEFAULT NULL,
  `street_number` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'Aarauu','CH','5000',9,'Schachenallee','29'),(2,'Altstätten','CH','9450',3,'Heidenerstrasse','5'),(3,'Solothurn','CH','4500',6,'Brühlstrassse','120'),(4,'weqweqw','ch','eqweqweq',1,'weqweqwe','qweqweqw'),(5,'asa','ch','1111',1,'asd','1'),(6,'asa','ch','1111',1,'asd','1'),(7,'asa','ch','1111',1,'asd','1'),(8,'asa','ch','1111',1,'asd','1'),(9,'asa','ch','1111',1,'asd','1'),(10,'Aarau','CH','5000',6,'Schwimmbadstrasse',''),(11,'Aarau','CH','5000',6,'Schwimmbadstrasse',''),(12,'Aarau','CH','5000',5,'Schwimmbadstrasse','7'),(13,'Solothurn','CH','5000',5,'Brühlstrassse','120'),(14,'Aarau','CH','5000',2,'Schachen','2'),(15,'Aarau','CH','5000',3,'Schachen','2'),(16,'Altstätten','CH','9450',3,'Sportplatz','2'),(17,'Altstätten','CH','9450',3,'Kriessernstrasse',''),(18,'Widnau','CH','9443',3,'Aegetenstrasse','60'),(23,'Solothurn','CH','4500',1,'street','2');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_datetime` datetime(3) DEFAULT NULL,
  `end_datetime` datetime(3) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `registration_code` varchar(20) DEFAULT NULL,
  `sponsor_text` varchar(250) DEFAULT NULL,
  `start_datetime` datetime(3) DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  `responsible_user_id` bigint DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_event_creator_id` (`creator_id`),
  KEY `FK_event_address_id` (`address_id`),
  KEY `FK_event_responsible_user_id` (`responsible_user_id`),
  CONSTRAINT `FK_event_address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FK_event_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_event_responsible_user_id` FOREIGN KEY (`responsible_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'2020-01-11 19:58:02.458','2020-05-10 08:00:00.000',33,22,'Schachen Aarau',20,'xxx','','2020-05-10 05:00:00.000',6,6,14),(2,'2020-01-12 10:19:39.170','2020-06-06 00:00:00.000',2,2,'Schachen Aarau',4,'xxxx','','2020-06-06 00:00:00.000',6,6,15),(3,'2020-02-20 13:02:34.693','2020-02-24 19:00:00.000',47.39093,9.589078,'Tennisturnier',2,'tennis','','2020-02-28 08:00:00.000',6,6,16),(10,'2020-01-12 13:02:34.693','2020-06-21 00:00:00.000',0,0,'Soloturn',2,'xxx','','2020-06-20 00:00:00.000',6,6,23);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_point`
--

DROP TABLE IF EXISTS `event_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lost_games_count` int DEFAULT NULL,
  `points` int DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `win_games_count` int DEFAULT NULL,
  `event_sport_id` bigint DEFAULT NULL,
  `opponent_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_event_point_user_id` (`user_id`),
  KEY `FK_event_point_opponent_id` (`opponent_id`),
  KEY `FK_event_point_event_sport_id` (`event_sport_id`),
  CONSTRAINT `FK_event_point_event_sport_id` FOREIGN KEY (`event_sport_id`) REFERENCES `event_sport` (`id`),
  CONSTRAINT `FK_event_point_opponent_id` FOREIGN KEY (`opponent_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_event_point_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_point`
--

LOCK TABLES `event_point` WRITE;
/*!40000 ALTER TABLE `event_point` DISABLE KEYS */;
INSERT INTO `event_point` VALUES (1,1,15,1,4,24,5,1),(2,2,22,1,3,24,5,5),(3,3,7,1,2,24,5,1),(4,4,14,1,1,25,5,2);
/*!40000 ALTER TABLE `event_point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_sport`
--

DROP TABLE IF EXISTS `event_sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_sport` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_version` int DEFAULT NULL,
  `event_id` bigint DEFAULT NULL,
  `sport_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_event_sport_sport_id` (`sport_id`),
  KEY `FK_event_sport_event_id` (`event_id`),
  CONSTRAINT `FK_event_sport_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FK_event_sport_sport_id` FOREIGN KEY (`sport_id`) REFERENCES `sport` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_sport`
--

LOCK TABLES `event_sport` WRITE;
/*!40000 ALTER TABLE `event_sport` DISABLE KEYS */;
INSERT INTO `event_sport` VALUES (1,1,1,2),(17,1,2,1),(20,1,1,1),(21,1,1,5),(22,1,2,5),(23,1,2,2),(24,1,10,1),(25,1,10,5),(26,1,10,2),(27,1,3,5);
/*!40000 ALTER TABLE `event_sport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_user`
--

DROP TABLE IF EXISTS `event_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_version` int DEFAULT NULL,
  `registration_datetime` datetime(3) NOT NULL,
  `event_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNQ_event_user_0` (`event_id`,`user_id`),
  KEY `FK_event_user_user_id` (`user_id`),
  CONSTRAINT `FK_event_user_event_id` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FK_event_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_user`
--

LOCK TABLES `event_user` WRITE;
/*!40000 ALTER TABLE `event_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `explorer_point`
--

DROP TABLE IF EXISTS `explorer_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `explorer_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `different_location_count` int DEFAULT NULL,
  `points` int DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_explorer_point_user_id` (`user_id`),
  CONSTRAINT `FK_explorer_point_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `explorer_point`
--

LOCK TABLES `explorer_point` WRITE;
/*!40000 ALTER TABLE `explorer_point` DISABLE KEYS */;
/*!40000 ALTER TABLE `explorer_point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `accept_datetime` datetime(3) DEFAULT NULL,
  `appointment_1_datetime` datetime(3) DEFAULT NULL,
  `appointment_2_datetime` datetime(3) DEFAULT NULL,
  `appointment_3_datetime` datetime(3) DEFAULT NULL,
  `count_for_leader_board` tinyint(1) DEFAULT '0',
  `creation_datetime` datetime(3) DEFAULT NULL,
  `execution_datetime` datetime(3) DEFAULT NULL,
  `game_state` varchar(12) DEFAULT NULL,
  `guest_score` int DEFAULT NULL,
  `has_host_made_appointments` tinyint(1) DEFAULT '0',
  `has_host_suggested_result` tinyint(1) DEFAULT '0',
  `host_score` int DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `result_record_datetime` datetime(3) DEFAULT NULL,
  `verified_datetime` datetime(3) DEFAULT NULL,
  `event_sport_id` bigint DEFAULT NULL,
  `guest_player_id` bigint DEFAULT NULL,
  `host_player_id` bigint DEFAULT NULL,
  `location_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_game_host_player_id` (`host_player_id`),
  KEY `FK_game_event_sport_id` (`event_sport_id`),
  KEY `FK_game_location_id` (`location_id`),
  KEY `FK_game_guest_player_id` (`guest_player_id`),
  CONSTRAINT `FK_game_event_sport_id` FOREIGN KEY (`event_sport_id`) REFERENCES `event_sport` (`id`),
  CONSTRAINT `FK_game_guest_player_id` FOREIGN KEY (`guest_player_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_game_host_player_id` FOREIGN KEY (`host_player_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_game_location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,'2019-11-16 00:00:00.000','2019-11-16 00:00:00.000','2019-11-16 00:00:00.000','2019-11-16 00:00:00.000',0,'2019-11-16 00:00:00.000','2019-11-20 00:00:00.000','PLAYED',8,0,0,5,4,'2019-11-16 00:00:00.000','2019-11-16 00:00:00.000',NULL,1,2,1),(2,'2019-11-17 00:00:00.000','2019-11-17 00:00:00.000','2019-11-17 00:00:00.000','2019-11-17 00:00:00.000',0,'2019-11-17 00:00:00.000','2019-11-18 00:00:00.000','RECORDED',12,0,0,6,10,'2019-11-17 00:00:00.000','2019-11-17 00:00:00.000',NULL,1,2,4),(3,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',1,'2019-10-17 00:00:00.000','2019-12-25 00:00:00.000','PLAYED',15,0,0,14,4,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',NULL,5,3,2),(4,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',1,'2019-10-17 00:00:00.000','2019-12-26 00:00:00.000','OPEN',NULL,0,0,NULL,3,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',NULL,NULL,3,2),(5,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',1,'2019-10-17 00:00:00.000','2019-12-26 00:00:00.000','OPEN',NULL,0,0,NULL,7,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',NULL,5,3,2),(18,NULL,NULL,NULL,NULL,1,'2019-12-28 01:37:23.396','2019-12-28 01:37:23.589','RECORDED',6,1,0,5,1,'2019-12-28 01:37:23.589',NULL,24,4,1,NULL),(19,NULL,NULL,NULL,NULL,1,'2019-12-28 01:37:23.396','2019-12-28 01:37:23.589','RECORDED',6,1,0,5,1,'2019-12-28 01:37:23.589',NULL,25,4,1,NULL),(20,NULL,NULL,NULL,NULL,1,'2019-12-28 01:37:23.396','2019-12-28 01:37:23.589','RECORDED',6,1,0,5,1,'2019-12-28 01:37:23.589',NULL,26,4,1,NULL);
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_datetime` datetime(3) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `loc_image` longblob,
  `longitude` double DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `verification_datetime` datetime(3) DEFAULT NULL,
  `player_id` bigint DEFAULT NULL,
  `location_state_id` bigint DEFAULT NULL,
  `sport_id` bigint DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_location_location_state_id` (`location_state_id`),
  KEY `FK_location_player_id` (`player_id`),
  KEY `FK_location_address_id` (`address_id`),
  KEY `FK_location_sport_id` (`sport_id`),
  CONSTRAINT `FK_location_address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FK_location_location_state_id` FOREIGN KEY (`location_state_id`) REFERENCES `location_state` (`id`),
  CONSTRAINT `FK_location_player_id` FOREIGN KEY (`player_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_location_sport_id` FOREIGN KEY (`sport_id`) REFERENCES `sport` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'2019-11-16 00:00:00.000',45.794,NULL,15.846,'Sporthalle Schachen',2,NULL,1,1,1,1),(2,'2019-11-16 00:00:00.000',45.799,NULL,15.873,'Schulhaus Feld',2,NULL,2,2,5,2),(3,'2019-11-16 00:00:00.000',45.797,NULL,15.858,'Schulhaus Brühl',4,NULL,3,3,3,3),(4,'2019-11-16 00:00:00.000',45.796,NULL,15.999,'Freibad Schachen',2,NULL,3,1,5,10),(5,'2019-11-16 00:00:00.000',45.771,NULL,15.886,'Leichtathletikstadion Schachen',2,NULL,3,1,2,11),(6,'2019-11-16 00:00:00.000',45.797,NULL,15.858,'Sportanlage Schachen',2,NULL,3,2,2,12),(7,'2019-11-16 00:00:00.000',45.699,NULL,15.903,'Schulhaus Brühl',2,NULL,3,1,1,13),(8,'2020-02-20 00:00:00.000',47.383971,NULL,9.547377,'GESA',2,NULL,3,1,2,16),(9,'2020-02-20 00:00:00.000',47.374386,NULL,9.561904,'Sportanlage Grüntal',2,NULL,3,1,3,17),(10,'2020-02-20 00:00:00.000',47.39449,NULL,9.639127,'Aegeten',2,NULL,3,1,4,18);
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_point`
--

DROP TABLE IF EXISTS `location_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lost_games_count` int DEFAULT NULL,
  `points` int DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `win_games_count` int DEFAULT NULL,
  `location_id` bigint DEFAULT NULL,
  `opponent_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_location_point_user_id` (`user_id`),
  KEY `FK_location_point_opponent_id` (`opponent_id`),
  KEY `FK_location_point_location_id` (`location_id`),
  CONSTRAINT `FK_location_point_location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_location_point_opponent_id` FOREIGN KEY (`opponent_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_location_point_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_point`
--

LOCK TABLES `location_point` WRITE;
/*!40000 ALTER TABLE `location_point` DISABLE KEYS */;
INSERT INTO `location_point` VALUES (1,0,12,1,1,1,2,1),(2,0,6,1,2,1,3,5),(3,0,100,1,3,2,4,1),(4,0,12,1,1,5,2,1),(5,0,6,1,2,5,3,5),(6,0,100,1,3,5,4,1);
/*!40000 ALTER TABLE `location_point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_state`
--

DROP TABLE IF EXISTS `location_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_state` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_state`
--

LOCK TABLES `location_state` WRITE;
/*!40000 ALTER TABLE `location_state` DISABLE KEYS */;
INSERT INTO `location_state` VALUES (1,'valid',1),(2,'invalid',1),(3,'to verify',1);
/*!40000 ALTER TABLE `location_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socializer_point`
--

DROP TABLE IF EXISTS `socializer_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socializer_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `different_opponent_count` int DEFAULT NULL,
  `points` int DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_socializer_point_user_id` (`user_id`),
  CONSTRAINT `FK_socializer_point_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socializer_point`
--

LOCK TABLES `socializer_point` WRITE;
/*!40000 ALTER TABLE `socializer_point` DISABLE KEYS */;
/*!40000 ALTER TABLE `socializer_point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sport`
--

DROP TABLE IF EXISTS `sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sport` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `icon_name` varchar(50) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `rules` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sport`
--

LOCK TABLES `sport` WRITE;
/*!40000 ALTER TABLE `sport` DISABLE KEYS */;
INSERT INTO `sport` VALUES
(7,'fas-dart','Dart',1,'none4'),
(8,'fas-table-football','Tischkicker',1,'none4'),
(9,'fas-boule','Boule',1,'none4'),
(10,'fas-morris','Mühle',1,'none4'),
(11,'fas-billiards','Billard',1,'none4');
/*!40000 ALTER TABLE `sport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `authenticated` tinyint(1) DEFAULT '0',
  `authorization_code` varchar(255) DEFAULT NULL,
  `blocked` tinyint(1) DEFAULT '0',
  `count_login` int DEFAULT NULL,
  `count_password_reset` int DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `display_image` longblob,
  `display_name` varchar(20) DEFAULT NULL,
  `firebase_user_device_token` varchar(200) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT '0',
  `is_verified` tinyint(1) DEFAULT '0',
  `last_name` varchar(50) DEFAULT NULL,
  `mobile_number` varchar(20) DEFAULT NULL,
  `password_hash` varchar(100) DEFAULT NULL,
  `receive_price_per_post` tinyint(1) DEFAULT '0',
  `record_version` int DEFAULT NULL,
  `register_datetime` datetime(3) DEFAULT NULL,
  `user_type` varchar(12) NOT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_address_id` (`address_id`),
  CONSTRAINT `FK_user_address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,0,NULL,0,0,0,0,NULL,'',NULL,'Paula',NULL,NULL,'Beck','0791234567','ziizuiopozippz',0,1,'2019-11-16 00:00:00.000','PLAYER','Paula',5),(2,0,NULL,0,0,0,0,NULL,'',NULL,'Peter',NULL,NULL,'Green','0191234568','ziizuiopozippz',0,1,'2019-11-16 00:00:00.000','PLAYER','Peter',6),(3,1,NULL,0,0,0,0,NULL,'',NULL,'Philip',NULL,NULL,'Black','0791111111','$2a$10$6mMWYXOJUsfzmWfg6Dx4euwb78KnnHmjiXXNLC6gq2zt5c5wHHigC',0,1,'2019-11-16 00:00:00.000','PLAYER','Phil',7),(4,1,NULL,0,0,0,0,NULL,'',NULL,'Andrey',NULL,NULL,'Black','0766666666','$2a$10$4eC7vU.ZE53dPXBBklg93eC3YzEbdd2DFzDUN0NEyDo02jbU.NNwa',0,1,'2019-11-16 00:00:00.000','PLAYER','Andrey',8),(5,1,NULL,0,0,0,0,NULL,'',NULL,'Wojtek',NULL,NULL,'Red','0762222222','$2a$10$YywH7GefWSLZ0ZP/h4zJkOdicYZwfgPtHXQoYweN8Eslqrg85WKfu',0,1,'2019-11-16 00:00:00.000','PLAYER','Wojtek',9),(6,1,NULL,0,0,0,0,NULL,'Admin',NULL,'aadmin',1,1,'aaadmin',NULL,'$2a$10$9suK3RmdVMpwd1RQ1h.4c.jbwEVsTEN655nyTYfPjco7DLzy7qx/O',0,0,'2019-11-16 00:00:00.000','PORTAL_USER','admin',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_point`
--

DROP TABLE IF EXISTS `user_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_point` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `points` int DEFAULT NULL,
  `record_version` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `sport_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_point_user_id` (`user_id`),
  KEY `FK_user_point_sport_id` (`sport_id`),
  CONSTRAINT `FK_user_point_sport_id` FOREIGN KEY (`sport_id`) REFERENCES `sport` (`id`),
  CONSTRAINT `FK_user_point_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_point`
--

LOCK TABLES `user_point` WRITE;
/*!40000 ALTER TABLE `user_point` DISABLE KEYS */;
INSERT INTO `user_point` VALUES (1,88,NULL,1,1),(2,66,NULL,1,2),(3,45,NULL,1,2),(4,36,NULL,5,1);
/*!40000 ALTER TABLE `user_point` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-21 20:06:56
