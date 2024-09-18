-- Drop user first if they exist
DROP USER if exists 'ecommercewebsite'@'%' ;

-- Now create user with prop privileges
CREATE USER 'ecommercewebsite'@'%' IDENTIFIED BY 'ecommercewebsite';

GRANT ALL PRIVILEGES ON * . * TO 'ecommercewebsite'@'%';

CREATE DATABASE IF NOT EXISTS `user_ecommerce_website`;

CREATE DATABASE IF NOT EXISTS `base_ecommerce_website`;

-- General Database
USE `base_ecommerce_website`;

CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `sku` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `unit_price` DECIMAL(13,2) DEFAULT NULL CHECK(unit_price >= 0),
  `image_url` VARCHAR(255) DEFAULT NULL,
  `active` BIT DEFAULT 1,
  `units_in_stock` INT(11) DEFAULT NULL,
  `category_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_category` (`category_id`),
  CONSTRAINT `fk_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `attribute` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `category_id` BIGINT(20),
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_category_attribute` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `product_attribute_value` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT(20),
  `attribute_id` BIGINT(20),
  `value` VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT `FK_product_attribute_value_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_product_attribute_value_attribute` FOREIGN KEY (`attribute_id`) REFERENCES `attribute` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS`customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone_number` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_tracking_number` varchar(255) DEFAULT NULL,
  `total_price` decimal(19,2) DEFAULT NULL,
  `total_quantity` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `shipping_address_id` bigint DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  `date_created` datetime(6) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `K_customer_id` (`customer_id`),
  CONSTRAINT `FK_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_shipping_address_id` FOREIGN KEY (`shipping_address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` decimal(19,2) DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `K_order_id` (`order_id`),
  CONSTRAINT `FK_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FK_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `wishlist` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_wishlist_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `Cart` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT DEFAULT NULL
)ENGINE=InnoDB AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `CartItem` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `cart_id` BIGINT DEFAULT NULL,
    `product_id` BIGINT DEFAULT NULL,
    `quantity` INT NOT NULL,
    FOREIGN KEY (`cart_id`) REFERENCES `Cart` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS Review (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT DEFAULT NULL,
    `user_id` BIGINT DEFAULT NULL,
    `rating` INT CHECK (rating >= 1 AND rating <= 5),
    `comment` TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT = 1;

-- User Database
USE `user_ecommerce_website`;

SET foreign_key_checks = 0;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `enabled` tinyint NOT NULL,
  `role` varchar(255) NOT NULL CHECK(role IN ('ADMIN', 'USER')),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `user_profile`;

CREATE TABLE `user_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone_number` VARCHAR(20) DEFAULT NULL,
  `user_id` int NOT NULL UNIQUE,
  PRIMARY KEY (`id`),
  KEY `FK_USER_idx` (`user_id`),
  CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

SET foreign_key_checks = 1;
SET FOREIGN_KEY_CHECKS = 1;
