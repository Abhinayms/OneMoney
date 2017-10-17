-- MySQL dump 10.13  Distrib 5.5.53, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: onemoneydb
-- ------------------------------------------------------
-- Server version	5.5.53-0ubuntu0.12.04.1

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `accountCode` varchar(255) DEFAULT NULL,
  `accountName` varchar(255) DEFAULT NULL,
  `hideFromBudget` tinyint(1) DEFAULT '0',
  `minBalance` float DEFAULT NULL,
  `purpose` enum('P','B') DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `accountTypeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_frnp5g2qs47uu764ln1rymxur` (`uuid`),
  KEY `FKt9fbxrxo3xvo4ucshaqt4922q` (`createdBy`),
  KEY `FKqkec0opgg5x8rxofo2w7ortqa` (`modifiedBy`),
  KEY `FK2ixkea52e2kir4tyl4fajsiwk` (`accountTypeId`),
  CONSTRAINT `FK2ixkea52e2kir4tyl4fajsiwk` FOREIGN KEY (`accountTypeId`) REFERENCES `accounttype` (`id`),
  CONSTRAINT `FKqkec0opgg5x8rxofo2w7ortqa` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKt9fbxrxo3xvo4ucshaqt4922q` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountAccountPropertyMapper`
--

DROP TABLE IF EXISTS `accountAccountPropertyMapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountAccountPropertyMapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `accountPropertyId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9hpxdx5om0m5h0lo22dxjtwl4` (`uuid`),
  KEY `FKkt740s2b0kywtow2x1huqokm3` (`createdBy`),
  KEY `FKchd1s0t6mbysgj4f5bjugoi4e` (`modifiedBy`),
  KEY `FKruu7f53vs583hkqvqyhh3yvuj` (`accountId`),
  KEY `FK2j8fi1otl2yi7oiduv8vn0eh5` (`accountPropertyId`),
  CONSTRAINT `FK2j8fi1otl2yi7oiduv8vn0eh5` FOREIGN KEY (`accountPropertyId`) REFERENCES `accountProperty` (`id`),
  CONSTRAINT `FKchd1s0t6mbysgj4f5bjugoi4e` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkt740s2b0kywtow2x1huqokm3` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKruu7f53vs583hkqvqyhh3yvuj` FOREIGN KEY (`accountId`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountAccountPropertyMapper`
--

LOCK TABLES `accountAccountPropertyMapper` WRITE;
/*!40000 ALTER TABLE `accountAccountPropertyMapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `accountAccountPropertyMapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountProperty`
--

DROP TABLE IF EXISTS `accountProperty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountProperty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_au091dllr5w5chckxmenx09a1` (`uuid`),
  KEY `FKpiap53vf5ms7jdve7abcgn4io` (`createdBy`),
  KEY `FK3g8dw6mm5ivokr4krknmbd2c7` (`modifiedBy`),
  KEY `FKs7a7uic4oo7tk75osf10nx8cb` (`accountId`),
  CONSTRAINT `FKs7a7uic4oo7tk75osf10nx8cb` FOREIGN KEY (`accountId`) REFERENCES `account` (`id`),
  CONSTRAINT `FK3g8dw6mm5ivokr4krknmbd2c7` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpiap53vf5ms7jdve7abcgn4io` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountProperty`
--

LOCK TABLES `accountProperty` WRITE;
/*!40000 ALTER TABLE `accountProperty` DISABLE KEYS */;
/*!40000 ALTER TABLE `accountProperty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountaccountgroupmapper`
--

DROP TABLE IF EXISTS `accountaccountgroupmapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountaccountgroupmapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `accountGroupId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dk6g8nojgj2yd1orgi9ykecn` (`uuid`),
  KEY `FKc9oc6ulaxnwvhvgaepa3aedkf` (`createdBy`),
  KEY `FKq0a9hoh0aiu3s3palxgsnpahg` (`modifiedBy`),
  KEY `FK86q9p7p2qifl4x194srgtiit6` (`accountId`),
  KEY `FKq5lp6d34ona84ptiohw217jj9` (`accountGroupId`),
  CONSTRAINT `FKq5lp6d34ona84ptiohw217jj9` FOREIGN KEY (`accountGroupId`) REFERENCES `accountgroup` (`id`),
  CONSTRAINT `FK86q9p7p2qifl4x194srgtiit6` FOREIGN KEY (`accountId`) REFERENCES `account` (`id`),
  CONSTRAINT `FKc9oc6ulaxnwvhvgaepa3aedkf` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKq0a9hoh0aiu3s3palxgsnpahg` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountaccountgroupmapper`
--

LOCK TABLES `accountaccountgroupmapper` WRITE;
/*!40000 ALTER TABLE `accountaccountgroupmapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `accountaccountgroupmapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountgroup`
--

DROP TABLE IF EXISTS `accountgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountgroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `instId` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `financialInstitutionId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n1w5b1n9un5py27f4f0kxhahs` (`uuid`),
  KEY `FK4wbxt0ypg225hspg7utlam4k0` (`createdBy`),
  KEY `FK316lqiqbtjvrolg5nn838pn9r` (`modifiedBy`),
  KEY `FKirsulshynbxiu10863wkkio87` (`financialInstitutionId`),
  CONSTRAINT `FKirsulshynbxiu10863wkkio87` FOREIGN KEY (`financialInstitutionId`) REFERENCES `financialinstitution` (`id`),
  CONSTRAINT `FK316lqiqbtjvrolg5nn838pn9r` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK4wbxt0ypg225hspg7utlam4k0` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountgroup`
--

LOCK TABLES `accountgroup` WRITE;
/*!40000 ALTER TABLE `accountgroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `accountgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounttype`
--

DROP TABLE IF EXISTS `accounttype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounttype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l0jnd3uwnu95avfdh0q4x36nu` (`uuid`),
  KEY `FK1t4sx63yq3qy0adodjvq1wwqb` (`createdBy`),
  KEY `FK12nlwmhbirc6ta88e3nao9anb` (`modifiedBy`),
  CONSTRAINT `FK12nlwmhbirc6ta88e3nao9anb` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK1t4sx63yq3qy0adodjvq1wwqb` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounttype`
--

LOCK TABLES `accounttype` WRITE;
/*!40000 ALTER TABLE `accounttype` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apicredentials`
--

DROP TABLE IF EXISTS `apicredentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apicredentials` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `apiId` varchar(255) DEFAULT NULL,
  `apiSecret` varchar(255) DEFAULT NULL,
  `isEnable` bit(1) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o8yeh1h8mv80d3m1ccblwxbhb` (`uuid`),
  UNIQUE KEY `UK_m9asp11g52ty2dekcur8mg63b` (`apiId`),
  UNIQUE KEY `UK_dblhrn0kwnx8vqmbcrys14r5n` (`apiSecret`),
  KEY `FK1fc30w0ef5pu7904qinw4c1fq` (`createdBy`),
  KEY `FKgy2fdtm5e02yc1x7oe06e16jq` (`modifiedBy`),
  KEY `FK5re8t1ac6bg7bbb61wx2eq4un` (`userId`),
  CONSTRAINT `FK5re8t1ac6bg7bbb61wx2eq4un` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK1fc30w0ef5pu7904qinw4c1fq` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKgy2fdtm5e02yc1x7oe06e16jq` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apicredentials`
--

LOCK TABLES `apicredentials` WRITE;
/*!40000 ALTER TABLE `apicredentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `apicredentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset`
--

DROP TABLE IF EXISTS `asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `growthRate` float NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `originalValuationDate` datetime DEFAULT NULL,
  `originalValue` float NOT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n9mrdc7dmulxf2c4xegoeaem3` (`uuid`),
  KEY `FKnuaqdejoju64b491jk1jn84a4` (`createdBy`),
  KEY `FKots966w5vsfd1bc0gw63eelhc` (`modifiedBy`),
  CONSTRAINT `FKots966w5vsfd1bc0gw63eelhc` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnuaqdejoju64b491jk1jn84a4` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset`
--

LOCK TABLES `asset` WRITE;
/*!40000 ALTER TABLE `asset` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budget`
--

DROP TABLE IF EXISTS `budget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budget` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `budgetedAmount` float DEFAULT NULL,
  `maxLimit` tinyint(1) DEFAULT '0',
  `purpose` enum('P','B') DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ks8p210djuwnkdax4hkxgwywj` (`uuid`),
  KEY `FKf4v324yoxt4abpg787g655g66` (`createdBy`),
  KEY `FKnuky3spba1eb9k3g8yb706wmc` (`modifiedBy`),
  KEY `FK5p7wpthfajsiwwrf142k8cyke` (`categoryId`),
  CONSTRAINT `FK5p7wpthfajsiwwrf142k8cyke` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`),
  CONSTRAINT `FKf4v324yoxt4abpg787g655g66` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnuky3spba1eb9k3g8yb706wmc` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budget`
--

LOCK TABLES `budget` WRITE;
/*!40000 ALTER TABLE `budget` DISABLE KEYS */;
/*!40000 ALTER TABLE `budget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `isUserCreated` tinyint(1) DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_31skolwu2k0hjbuy675bv33po` (`uuid`),
  KEY `FKeljfpih1vc5nxhgh8qhh52i1k` (`createdBy`),
  KEY `FKs7b75ak4sy5468hcqxq9ios2i` (`modifiedBy`),
  CONSTRAINT `FKs7b75ak4sy5468hcqxq9ios2i` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKeljfpih1vc5nxhgh8qhh52i1k` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorytransactionmapper`
--

DROP TABLE IF EXISTS `categorytransactionmapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categorytransactionmapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL,
  `transactionId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_7khpl1gj467aehtilnm3hsy5v` (`uuid`),
  KEY `FK7l58941nv8h61myo8odfga2yw` (`createdBy`),
  KEY `FK8lyrwd24cfwcb949eku77pkh0` (`modifiedBy`),
  KEY `FKm5edh6xx1jgijq1lddn8bluof` (`accountId`),
  KEY `FK4slr248ni7hu6wnuivfn472a3` (`categoryId`),
  KEY `FK8a8f8ym6wlj6kpjctoh8o67kk` (`transactionId`),
  CONSTRAINT `FK8a8f8ym6wlj6kpjctoh8o67kk` FOREIGN KEY (`transactionId`) REFERENCES `transaction` (`id`),
  CONSTRAINT `FK4slr248ni7hu6wnuivfn472a3` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`),
  CONSTRAINT `FK7l58941nv8h61myo8odfga2yw` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK8lyrwd24cfwcb949eku77pkh0` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKm5edh6xx1jgijq1lddn8bluof` FOREIGN KEY (`accountId`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorytransactionmapper`
--

LOCK TABLES `categorytransactionmapper` WRITE;
/*!40000 ALTER TABLE `categorytransactionmapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `categorytransactionmapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `configLabelName` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `divisionId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rkm3g5aw9qjrjmd0ty9lgibyn` (`uuid`),
  KEY `FKfd69br5h07bfbvc6ydjkoxfv` (`createdBy`),
  KEY `FKtg5qbn1y33xafglw9u7fkocut` (`modifiedBy`),
  KEY `FKbb8w0smqhh4i5kikhr1nwtjie` (`divisionId`),
  CONSTRAINT `FKbb8w0smqhh4i5kikhr1nwtjie` FOREIGN KEY (`divisionId`) REFERENCES `division` (`id`),
  CONSTRAINT `FKfd69br5h07bfbvc6ydjkoxfv` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKtg5qbn1y33xafglw9u7fkocut` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `division` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `configLabelName` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `entityId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_226rs2jl1tkv334d3nk0968oe` (`uuid`),
  KEY `FK7u8uw6m15k1ugqy6l0fbdtxnr` (`createdBy`),
  KEY `FK85ftgph25grc4dgwqvidibfue` (`modifiedBy`),
  KEY `FK1rvt9p1ma4fhke71f04hk9j3w` (`entityId`),
  CONSTRAINT `FK1rvt9p1ma4fhke71f04hk9j3w` FOREIGN KEY (`entityId`) REFERENCES `entity` (`id`),
  CONSTRAINT `FK7u8uw6m15k1ugqy6l0fbdtxnr` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK85ftgph25grc4dgwqvidibfue` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity`
--

DROP TABLE IF EXISTS `entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `configLabelName` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `organisationId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ckkkpbd1ayrdgjc77w54r8qp5` (`uuid`),
  KEY `FKca79t69456u4l7kr2d0sppybh` (`createdBy`),
  KEY `FKdpk03uqy783clmiv3n166u7v2` (`modifiedBy`),
  KEY `FK3bcse3mythimo4r61ahqd8sm1` (`organisationId`),
  CONSTRAINT `FK3bcse3mythimo4r61ahqd8sm1` FOREIGN KEY (`organisationId`) REFERENCES `organisation` (`id`),
  CONSTRAINT `FKca79t69456u4l7kr2d0sppybh` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKdpk03uqy783clmiv3n166u7v2` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity`
--

LOCK TABLES `entity` WRITE;
/*!40000 ALTER TABLE `entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financialinstitution`
--

DROP TABLE IF EXISTS `financialinstitution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `financialinstitution` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `instCode` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lbhv3wkj20aq8pmxo9wps9hsb` (`uuid`),
  KEY `FK7dlq4h939di6e2t348nix7773` (`createdBy`),
  KEY `FKcbppxrpr1mw2j8ldi3t3df2qa` (`modifiedBy`),
  CONSTRAINT `FKcbppxrpr1mw2j8ldi3t3df2qa` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK7dlq4h939di6e2t348nix7773` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financialinstitution`
--

LOCK TABLES `financialinstitution` WRITE;
/*!40000 ALTER TABLE `financialinstitution` DISABLE KEYS */;
/*!40000 ALTER TABLE `financialinstitution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frequencyType`
--

DROP TABLE IF EXISTS `frequencyType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `frequencyType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `noOfMonths` int(11) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1fhbjkoxk9c1e0r464dk11qfs` (`uuid`),
  KEY `FK191j59pab51wje2fwlu62e2pa` (`createdBy`),
  KEY `FKn5hq182rdaor6hslr0vvo8k7u` (`modifiedBy`),
  CONSTRAINT `FKn5hq182rdaor6hslr0vvo8k7u` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK191j59pab51wje2fwlu62e2pa` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frequencyType`
--

LOCK TABLES `frequencyType` WRITE;
/*!40000 ALTER TABLE `frequencyType` DISABLE KEYS */;
/*!40000 ALTER TABLE `frequencyType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal`
--

DROP TABLE IF EXISTS `goal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `goalAchieved` tinyint(1) DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `installmentAmount` float NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `purpose` enum('P','B') DEFAULT NULL,
  `targetAmount` float NOT NULL,
  `targetDate` datetime DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `frequencyTypeId` bigint(20) DEFAULT NULL,
  `goalTypeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_se4eupfxps3qtcqccn39bbpud` (`uuid`),
  KEY `FKnrsf32ec149fpvkwvvnj9et3f` (`createdBy`),
  KEY `FKgs9e75gfqv4crn98bgk3a1ygq` (`modifiedBy`),
  KEY `FK8s0ycsp26oopawt3a8182ojog` (`frequencyTypeId`),
  KEY `FKanq06isv1j0a04k1ghe5vwjyw` (`goalTypeId`),
  CONSTRAINT `FKanq06isv1j0a04k1ghe5vwjyw` FOREIGN KEY (`goalTypeId`) REFERENCES `goalType` (`id`),
  CONSTRAINT `FK8s0ycsp26oopawt3a8182ojog` FOREIGN KEY (`frequencyTypeId`) REFERENCES `frequencyType` (`id`),
  CONSTRAINT `FKgs9e75gfqv4crn98bgk3a1ygq` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnrsf32ec149fpvkwvvnj9et3f` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal`
--

LOCK TABLES `goal` WRITE;
/*!40000 ALTER TABLE `goal` DISABLE KEYS */;
/*!40000 ALTER TABLE `goal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goalAccountMapper`
--

DROP TABLE IF EXISTS `goalAccountMapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goalAccountMapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `proportion` float DEFAULT NULL,
  `rateOfInterest` float DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `goalId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_m12rnx546yxku4tcj5emh9pd` (`uuid`),
  KEY `FKtljntivr4k62cj1jglacblqhg` (`createdBy`),
  KEY `FKblqovvmk2hauw461q3ront4ys` (`modifiedBy`),
  KEY `FKfljcf2ruyiiodgp8r7v55h9a2` (`accountId`),
  KEY `FKlvmcrhu96b1q4jpt5j0njgdof` (`goalId`),
  CONSTRAINT `FKlvmcrhu96b1q4jpt5j0njgdof` FOREIGN KEY (`goalId`) REFERENCES `goal` (`id`),
  CONSTRAINT `FKblqovvmk2hauw461q3ront4ys` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKfljcf2ruyiiodgp8r7v55h9a2` FOREIGN KEY (`accountId`) REFERENCES `account` (`id`),
  CONSTRAINT `FKtljntivr4k62cj1jglacblqhg` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goalAccountMapper`
--

LOCK TABLES `goalAccountMapper` WRITE;
/*!40000 ALTER TABLE `goalAccountMapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `goalAccountMapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goalAssetMapper`
--

DROP TABLE IF EXISTS `goalAssetMapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goalAssetMapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `proportion` float DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `assetId` bigint(20) DEFAULT NULL,
  `goalId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_22b5w34c7ygocjfjso0qgpy6w` (`uuid`),
  KEY `FKke7qoh7nsbtbwf5mpxplp8hd7` (`createdBy`),
  KEY `FK8ym48ni3v489vb6gd7shap5qc` (`modifiedBy`),
  KEY `FKk0ttngqqlri48ie7o7xtem743` (`assetId`),
  KEY `FKbthn05klvg6rivbfup1plpkha` (`goalId`),
  CONSTRAINT `FKbthn05klvg6rivbfup1plpkha` FOREIGN KEY (`goalId`) REFERENCES `goal` (`id`),
  CONSTRAINT `FK8ym48ni3v489vb6gd7shap5qc` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKk0ttngqqlri48ie7o7xtem743` FOREIGN KEY (`assetId`) REFERENCES `asset` (`id`),
  CONSTRAINT `FKke7qoh7nsbtbwf5mpxplp8hd7` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goalAssetMapper`
--

LOCK TABLES `goalAssetMapper` WRITE;
/*!40000 ALTER TABLE `goalAssetMapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `goalAssetMapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goalType`
--

DROP TABLE IF EXISTS `goalType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goalType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_la8otvfmcin2f5mwmoge4f38n` (`uuid`),
  KEY `FKtopun49uic5pm1i6thp3gsslv` (`createdBy`),
  KEY `FKlqe91c547j0g7oler93uwdih0` (`modifiedBy`),
  CONSTRAINT `FKlqe91c547j0g7oler93uwdih0` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKtopun49uic5pm1i6thp3gsslv` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goalType`
--

LOCK TABLES `goalType` WRITE;
/*!40000 ALTER TABLE `goalType` DISABLE KEYS */;
/*!40000 ALTER TABLE `goalType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grouprolemapper`
--

DROP TABLE IF EXISTS `grouprolemapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grouprolemapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `groupId` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8kbw2ux3u0v2xiamsygacx11c` (`uuid`),
  KEY `FKceed52qtlwovqh9k8nh4gildy` (`createdBy`),
  KEY `FKecicrwfeqqn42xqhxmgxx0o8f` (`modifiedBy`),
  KEY `FKj86kjplscw1kno1i74ued5ran` (`groupId`),
  KEY `FK3va5y44huuyuir90m8uhe8h0k` (`roleId`),
  CONSTRAINT `FK3va5y44huuyuir90m8uhe8h0k` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `FKceed52qtlwovqh9k8nh4gildy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKecicrwfeqqn42xqhxmgxx0o8f` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj86kjplscw1kno1i74ued5ran` FOREIGN KEY (`groupId`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grouprolemapper`
--

LOCK TABLES `grouprolemapper` WRITE;
/*!40000 ALTER TABLE `grouprolemapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `grouprolemapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bp1v596yvy8vokx7dex2o3jbb` (`uuid`),
  KEY `FKfla987q147tsker17bxv93qtx` (`createdBy`),
  KEY `FK2sdymk8bbkule0kreei5inli` (`modifiedBy`),
  CONSTRAINT `FK2sdymk8bbkule0kreei5inli` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKfla987q147tsker17bxv93qtx` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loginattempts`
--

DROP TABLE IF EXISTS `loginattempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loginattempts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ipAddress` varchar(255) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_smowuxno8beow6q3b9laapvkv` (`uuid`),
  KEY `FK5qa1donch8yhk5l173gvfqqb0` (`createdBy`),
  KEY `FKcrnn10v4bdsfbs84ki9i5ob03` (`modifiedBy`),
  KEY `FK8q5gbhf2fy12lur5n5vg4wi73` (`userId`),
  CONSTRAINT `FK8q5gbhf2fy12lur5n5vg4wi73` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK5qa1donch8yhk5l173gvfqqb0` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKcrnn10v4bdsfbs84ki9i5ob03` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loginattempts`
--

LOCK TABLES `loginattempts` WRITE;
/*!40000 ALTER TABLE `loginattempts` DISABLE KEYS */;
/*!40000 ALTER TABLE `loginattempts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation`
--

DROP TABLE IF EXISTS `organisation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `configLabelName` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a0spar1od6i2qp7n3b3l0ug5y` (`uuid`),
  KEY `FK127tx87rk6x4oap00de297mwo` (`createdBy`),
  KEY `FKpo4svawyjgs8euab394acro73` (`modifiedBy`),
  CONSTRAINT `FKpo4svawyjgs8euab394acro73` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK127tx87rk6x4oap00de297mwo` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation`
--

LOCK TABLES `organisation` WRITE;
/*!40000 ALTER TABLE `organisation` DISABLE KEYS */;
/*!40000 ALTER TABLE `organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `urlPattern` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_demnsbulyf6plpowb2cfn1gen` (`uuid`),
  KEY `FK1thlq4d8h2nmmqxqy6opj82xl` (`createdBy`),
  KEY `FKo001o2htdsuoay5hbfm2rmd81` (`modifiedBy`),
  CONSTRAINT `FKo001o2htdsuoay5hbfm2rmd81` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FK1thlq4d8h2nmmqxqy6opj82xl` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resources`
--

DROP TABLE IF EXISTS `resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resources` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `resourceType` bit(1) NOT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gitf76csuxp5v66ny4b3f778m` (`uuid`),
  KEY `FKs7c8xhy79u0y8pu8l4913i3ls` (`createdBy`),
  KEY `FK63fop0tv71wsegkhct50hlcc8` (`modifiedBy`),
  CONSTRAINT `FK63fop0tv71wsegkhct50hlcc8` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKs7c8xhy79u0y8pu8l4913i3ls` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resources`
--

LOCK TABLES `resources` WRITE;
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k5dwya5n8n7y3m2opvmm7qjcc` (`uuid`),
  KEY `FKnss8q8xknag2srmok5k8x7l76` (`createdBy`),
  KEY `FKiwl8o8ljj7u9sf5soc9k2n4hb` (`modifiedBy`),
  CONSTRAINT `FKiwl8o8ljj7u9sf5soc9k2n4hb` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnss8q8xknag2srmok5k8x7l76` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolepermissionmapper`
--

DROP TABLE IF EXISTS `rolepermissionmapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolepermissionmapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `permissionId` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mwh5000on97ojgcs86hwv9r9y` (`uuid`),
  KEY `FK1rln7ndq73icrv5lprsl0ygqo` (`createdBy`),
  KEY `FKkpjwf78x91kf4p3ot4c1qi6dj` (`modifiedBy`),
  KEY `FKsokoxms06lidgavqb3rwvou6i` (`permissionId`),
  KEY `FKkd3r0e5b3g1c780mm98kguteq` (`roleId`),
  CONSTRAINT `FKkd3r0e5b3g1c780mm98kguteq` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `FK1rln7ndq73icrv5lprsl0ygqo` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkpjwf78x91kf4p3ot4c1qi6dj` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKsokoxms06lidgavqb3rwvou6i` FOREIGN KEY (`permissionId`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolepermissionmapper`
--

LOCK TABLES `rolepermissionmapper` WRITE;
/*!40000 ALTER TABLE `rolepermissionmapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `rolepermissionmapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sgintegration`
--

DROP TABLE IF EXISTS `sgintegration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sgintegration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `appId` varchar(255) DEFAULT NULL,
  `appSecret` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `endPoint` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l2qb578e8qfbbu4ox5wcyc9d` (`uuid`),
  KEY `FKi1v89lshbtn11ou0g8knj18x8` (`createdBy`),
  KEY `FKmv16oktxa4s9027c2xedgvewp` (`modifiedBy`),
  CONSTRAINT `FKmv16oktxa4s9027c2xedgvewp` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKi1v89lshbtn11ou0g8knj18x8` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sgintegration`
--

LOCK TABLES `sgintegration` WRITE;
/*!40000 ALTER TABLE `sgintegration` DISABLE KEYS */;
/*!40000 ALTER TABLE `sgintegration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `splittransactions`
--

DROP TABLE IF EXISTS `splittransactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `splittransactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL,
  `transactionId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_s896lyeu3bpq89fvrk3t1enld` (`uuid`),
  KEY `FK1v8ii1jub5hbmjytk8oev2ewb` (`createdBy`),
  KEY `FKtgo23ru5ivglay3vpb33oelr1` (`modifiedBy`),
  KEY `FKmh23x9lvmynvl2snb4iiubvt` (`categoryId`),
  KEY `FKgeons9tx4ofbm8tn2i1p6wyrg` (`transactionId`),
  CONSTRAINT `FKgeons9tx4ofbm8tn2i1p6wyrg` FOREIGN KEY (`transactionId`) REFERENCES `transaction` (`id`),
  CONSTRAINT `FK1v8ii1jub5hbmjytk8oev2ewb` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKmh23x9lvmynvl2snb4iiubvt` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`),
  CONSTRAINT `FKtgo23ru5ivglay3vpb33oelr1` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `splittransactions`
--

LOCK TABLES `splittransactions` WRITE;
/*!40000 ALTER TABLE `splittransactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `splittransactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `accountCode` varchar(255) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fingerprint` varchar(255) DEFAULT NULL,
  `isCategorized` tinyint(1) DEFAULT '0',
  `isSplitted` tinyint(1) DEFAULT '0',
  `transactionDate` datetime DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t6am1c6a3i6d9qgvlq219hsx4` (`uuid`),
  KEY `FKpmglpxayukw9d26kypk3sfybo` (`createdBy`),
  KEY `FKn08s6dohjx80jfnnp8ik1e6em` (`modifiedBy`),
  CONSTRAINT `FKn08s6dohjx80jfnnp8ik1e6em` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpmglpxayukw9d26kypk3sfybo` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `avtar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `logInFailureAttempts` int(11) DEFAULT NULL,
  `middleName` varchar(255) DEFAULT NULL,
  `countryCode` varchar(255) NOT NULL,
  `mobile` varchar(255) NOT NULL,  
  `password` varchar(255) NOT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cnjwxx5favk5ycqajjt17fwy1` (`mobile`),
  UNIQUE KEY `UK_1xc1iry6gqjrvh5cpajiq7l2f` (`uuid`),
  KEY `FKt8b01a13ndrte11q8od3oa8pk` (`createdBy`),
  KEY `FKau35pwmd3omx41bww5oq8yxl5` (`modifiedBy`),
  CONSTRAINT `FKau35pwmd3omx41bww5oq8yxl5` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKt8b01a13ndrte11q8od3oa8pk` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userDetails`
--

DROP TABLE IF EXISTS `userDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userDetails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `businessMonthlyIncome` float DEFAULT NULL,
  `personalMonthlyIncome` float DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_fwfoj8hun50wxda6cl4mlfyj` (`uuid`),
  KEY `FK2jurm7wqgw5bjybh535a6c7xf` (`createdBy`),
  KEY `FKfs7alpb6q9j99jpdnjdj3rs70` (`modifiedBy`),
  KEY `FKjqakn1u3pq2dhev265jrq68sd` (`userId`),
  CONSTRAINT `FKjqakn1u3pq2dhev265jrq68sd` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2jurm7wqgw5bjybh535a6c7xf` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKfs7alpb6q9j99jpdnjdj3rs70` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userDetails`
--

LOCK TABLES `userDetails` WRITE;
/*!40000 ALTER TABLE `userDetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `userDetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usergroupmapper`
--

DROP TABLE IF EXISTS `usergroupmapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usergroupmapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `groupId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2fqvbifsfougsumt1ybqebl30` (`uuid`),
  KEY `FKhvg7hl1ps3tauaqvk2kc5uus3` (`createdBy`),
  KEY `FK3dv3e6dumb7aaal5mleolmt35` (`modifiedBy`),
  KEY `FKenegym6bs97jcsopl4fd9m75q` (`groupId`),
  KEY `FKkoh8k50ttrleqni7m85sahxay` (`userId`),
  CONSTRAINT `FKkoh8k50ttrleqni7m85sahxay` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK3dv3e6dumb7aaal5mleolmt35` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKenegym6bs97jcsopl4fd9m75q` FOREIGN KEY (`groupId`) REFERENCES `groups` (`id`),
  CONSTRAINT `FKhvg7hl1ps3tauaqvk2kc5uus3` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usergroupmapper`
--

LOCK TABLES `usergroupmapper` WRITE;
/*!40000 ALTER TABLE `usergroupmapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `usergroupmapper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usernotifications`
--

DROP TABLE IF EXISTS `usernotifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usernotifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `isShown` bit(1) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lh34notblmtdilqqliym7to3f` (`uuid`),
  KEY `FKc3dsedfp53hic7mwwc7lxh0ax` (`createdBy`),
  KEY `FKsx2mxe1bjpiecgoocuak58han` (`modifiedBy`),
  KEY `FK4kq9du4w86967pg0di1j76epl` (`userId`),
  CONSTRAINT `FK4kq9du4w86967pg0di1j76epl` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FKc3dsedfp53hic7mwwc7lxh0ax` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKsx2mxe1bjpiecgoocuak58han` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usernotifications`
--

LOCK TABLES `usernotifications` WRITE;
/*!40000 ALTER TABLE `usernotifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `usernotifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userrequests`
--

DROP TABLE IF EXISTS `userrequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userrequests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `isComplete` tinyint(1) DEFAULT '0',
  `lifeOfRequest` datetime DEFAULT NULL,
  `requestKey` varchar(255) DEFAULT NULL,
  `requestType` enum('activation','forgotPassword') DEFAULT NULL,
  `requestingIPAddress` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jqs0wq9husho3i6824les9wvh` (`uuid`),
  KEY `FK5m7frf8gl7ijcq0c2xqa7fat6` (`createdBy`),
  KEY `FKg5kouxo841689s3eui0bvaawd` (`modifiedBy`),
  KEY `FK694e9lvb7e2hwxaa0rwgbvj8v` (`userId`),
  CONSTRAINT `FK694e9lvb7e2hwxaa0rwgbvj8v` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK5m7frf8gl7ijcq0c2xqa7fat6` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKg5kouxo841689s3eui0bvaawd` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userrequests`
--

LOCK TABLES `userrequests` WRITE;
/*!40000 ALTER TABLE `userrequests` DISABLE KEYS */;
/*!40000 ALTER TABLE `userrequests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userrolemapper`
--

DROP TABLE IF EXISTS `userrolemapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userrolemapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT '1',
  `modifiedDate` datetime DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `createdBy` bigint(20) DEFAULT NULL,
  `modifiedBy` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_riiyv3g0mdqynchdkyalicqps` (`uuid`),
  KEY `FKx1i31i194v1xeejql98ju5qs` (`createdBy`),
  KEY `FKagxtgvquxq64k24pgudjnfllu` (`modifiedBy`),
  KEY `FK9c97bbxt5etns481r6rnfyf6k` (`roleId`),
  KEY `FK3lr80u3qwm5vsg7t5wv35njes` (`userId`),
  CONSTRAINT `FK3lr80u3qwm5vsg7t5wv35njes` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK9c97bbxt5etns481r6rnfyf6k` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `FKagxtgvquxq64k24pgudjnfllu` FOREIGN KEY (`modifiedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `FKx1i31i194v1xeejql98ju5qs` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userrolemapper`
--

LOCK TABLES `userrolemapper` WRITE;
/*!40000 ALTER TABLE `userrolemapper` DISABLE KEYS */;
/*!40000 ALTER TABLE `userrolemapper` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-01 12:06:21