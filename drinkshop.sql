-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: drinkstore
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
-- Table structure for table `blogs`
--

DROP TABLE IF EXISTS `blogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blogs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `content` text,
  `summary` text,
  `thumbnail` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpg4damav6db6a6fh5peylcni5` (`user_id`),
  CONSTRAINT `FKpg4damav6db6a6fh5peylcni5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blogs`
--

LOCK TABLES `blogs` WRITE;
/*!40000 ALTER TABLE `blogs` DISABLE KEYS */;
/*!40000 ALTER TABLE `blogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `description` tinytext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Various types of coffee drinks','Coffee'),(2,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Green tea, black tea, herbal tea','Tea'),(3,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Fresh fruit juices','Juice'),(4,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Fruit smoothies and blended drinks','Smoothies'),(5,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Soft drinks and soda beverages','Soda'),(6,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Energy boosting drinks','Energy Drinks'),(7,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Bubble tea and milk tea drinks','Milk Tea'),(8,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Fresh milk and flavored milk','Milk'),(9,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Alcoholic mixed drinks','Cocktails'),(10,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Non-alcoholic mocktails','Mocktails'),(11,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Mineral water and flavored water','Water'),(12,_binary '','2025-04-27 11:11:13.000000','system',_binary '\0','2025-04-27 04:11:13','system','Hot chocolate drinks','Hot Chocolate');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `content` text,
  `blog_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9aakob3a7aghrm94k9kmbrjqd` (`blog_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9aakob3a7aghrm94k9kmbrjqd` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` tinytext,
  `name` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drinks`
--

DROP TABLE IF EXISTS `drinks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drinks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `buy_count` int DEFAULT NULL,
  `cover_image` varchar(255) DEFAULT NULL,
  `description` text NOT NULL,
  `volume_ml` int DEFAULT NULL,
  `original_price` double DEFAULT NULL,
  `published_date` datetime(6) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `sale_price` double DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `total_revenue` double DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKivr4pooetq4771isaglt2j0br` (`category_id`),
  CONSTRAINT `FKivr4pooetq4771isaglt2j0br` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drinks`
--

LOCK TABLES `drinks` WRITE;
/*!40000 ALTER TABLE `drinks` DISABLE KEYS */;
INSERT INTO `drinks` VALUES (1,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand A',100,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Delicious espresso coffee.',250,50,'2025-04-27 11:12:48.000000','Coffee Inc.',100,45000,'Espresso',4500000,1),(2,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand B',80,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Freshly brewed Americano.',300,55,'2025-04-27 11:12:48.000000','Coffee Co.',120,50000,'Americano',4100000,1),(3,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand C',120,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Smooth latte coffee.',350,60,'2025-04-27 11:12:48.000000','Latte House',90,55000,'Latte',4950000,1),(4,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand D',90,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Cappuccino with rich foam.',300,65,'2025-04-27 11:12:48.000000','Cappuccino Ltd.',85,58000,'Cappuccino',5220000,1),(5,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand E',60,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Sweet caramel macchiato.',400,70,'2025-04-27 11:12:48.000000','Macchiato Co.',110,65000,'Caramel Macchiato',3900000,1),(6,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand F',75,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Refreshing cold brew.',450,80,'2025-04-27 11:12:48.000000','Brew Co.',130,75000,'Cold Brew',5625000,1),(7,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand G',85,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Rich mocha flavor.',350,85,'2025-04-27 11:12:48.000000','Mocha Bros.',95,80000,'Mocha',6800000,1),(8,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand H',70,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Vanilla flavored coffee.',300,60,'2025-04-27 11:12:48.000000','Vanilla Beans',100,55000,'Vanilla Coffee',3850000,1),(9,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand I',95,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Hazelnut coffee drink.',300,65,'2025-04-27 11:12:48.000000','Hazelnut House',105,60000,'Hazelnut Coffee',5700000,1),(10,_binary '','2025-04-27 11:12:48.000000','system',_binary '\0','2025-04-27 04:12:48','system','Brand J',100,'https://baristaschool.vn/wp-content/uploads/2023/09/Americano.png','Strong black coffee.',250,45,'2025-04-27 11:12:48.000000','Black Coffee Co.',120,40000,'Black Coffee',4000000,1);
/*!40000 ALTER TABLE `drinks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite_items`
--

DROP TABLE IF EXISTS `favorite_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_items` (
  `user_id` bigint NOT NULL,
  `drink_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`drink_id`),
  KEY `FK7n1mg2jlukwyrcifkfvsqju07` (`drink_id`),
  CONSTRAINT `FK7n1mg2jlukwyrcifkfvsqju07` FOREIGN KEY (`drink_id`) REFERENCES `drinks` (`id`),
  CONSTRAINT `FKmcicth3vly9ytekmo9es5vtri` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite_items`
--

LOCK TABLES `favorite_items` WRITE;
/*!40000 ALTER TABLE `favorite_items` DISABLE KEYS */;
INSERT INTO `favorite_items` VALUES (1,2),(2,2);
/*!40000 ALTER TABLE `favorite_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `drink_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`drink_id`,`order_id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  CONSTRAINT `FK3m51b6x5qdybtycvuff81g1sh` FOREIGN KEY (`drink_id`) REFERENCES `drinks` (`id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (2,1,50000,2),(2,2,50000,3);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `reciever` varchar(255) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `vnp_txn_ref` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,_binary '','2025-04-27 11:41:53.372000',NULL,_binary '\0',NULL,NULL,'DRINK-ULL4IUVV06','admin@gmail.com','COD','0123456789','Admin','hn','PENDING',100000,NULL,1),(2,_binary '','2025-04-27 11:57:52.804000',NULL,_binary '\0',NULL,NULL,'DRINK-OZ2XKJZ11Z','trungduc@gmail.com','VNPAY','0123456789','Trung Duc','HN','PENDING',150000,NULL,2);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,NULL,NULL,NULL,NULL,'2025-04-27 04:37:58',NULL,'ROLE_ADMIN'),(2,NULL,NULL,NULL,NULL,'2025-04-27 04:37:58',NULL,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_flag` bit(1) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `delete_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `status` smallint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '',NULL,NULL,NULL,'2025-04-27 04:39:22',NULL,'hn','admin@gmail.com','Admin','$2a$10$uD4oIEVYq3LgFGyltVteHet/MvBZHs0CLpXIlOdJbZqtt4GWjdUQa','0123456789',1),(2,_binary '',NULL,NULL,NULL,'2025-04-27 04:39:22',NULL,'hn','trungduc@gmail.com','Trung Duc','$2a$10$uD4oIEVYq3LgFGyltVteHet/MvBZHs0CLpXIlOdJbZqtt4GWjdUQa','0123456789',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (1,1),(1,2),(2,2);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-04 10:20:22
