
CREATE TABLE IF NOT EXISTS `adf_results` (
  `commodity_id` bigint NOT NULL,
  `adf_statistic` double DEFAULT NULL,
  `p_value` double DEFAULT NULL,
  `critical_value_1_percent` double DEFAULT NULL,
  `critical_value_5_percent` double DEFAULT NULL,
  `critical_value_10_percent` double DEFAULT NULL,
  `adf_resultscol` varchar(45) DEFAULT NULL,
  `is_stationary` int DEFAULT NULL,
  PRIMARY KEY (`commodity_id`)
);


CREATE TABLE IF NOT EXISTS `commodities` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `data_points` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `commodity_id` bigint DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  `value` double DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `holtwinters` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `commodity_id` bigint DEFAULT NULL,
  `forecast_date` date DEFAULT NULL,
  `forecast_value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `sarimax` (
  `id` int NOT NULL AUTO_INCREMENT,
  `commodity_id` int DEFAULT NULL,
  `forecast_date` date DEFAULT NULL,
  `forecast_value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`)
);

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);
