INSERT INTO `8b_sp_sicobo`.`users` (`email`, `lastname`, `name`, `number_attempts`, `password`, `phone_number`, `policy_acceptance`, `rfc`, `role`, `enabled`, `surname`, `username`) VALUES ('hola@gmail.com', 'dasdas', 'gfgf', '0', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '7775996014', '1', '1', 'ROLE_ADMIN', '1', 'dasda', 'admin');
INSERT INTO `8b_sp_sicobo`.`authorities` (`password`, `authority`, `enabled`, `username`) VALUES ('$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a','ROLE_ADMIN', 1, 'admin');


INSERT INTO `8b_sp_sicobo`.`users` (`email`, `lastname`, `name`, `number_attempts`, `password`, `phone_number`, `policy_acceptance`, `rfc`, `role`, `enabled`, `surname`, `username`) VALUES ('hola@gmail.com', 'dasdas', 'gfgf', '0', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '7775996014', '1', '1', 'ROLE_GESTOR', '1', 'dasda', 'gestor');
INSERT INTO `8b_sp_sicobo`.`authorities` (`password`, `authority`, `enabled`, `username`) VALUES ('$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a','ROLE_GESTOR', 1, 'gestor');

INSERT INTO `8b_sp_sicobo`.`users` (`email`, `lastname`, `name`, `number_attempts`, `password`, `phone_number`, `policy_acceptance`, `rfc`, `role`, `enabled`, `surname`, `username`) VALUES ('hola@gmail.com', 'dasdas', 'gfgf', '0', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '7775996014', '1', '1', 'ROLE_USUARIO', '1', 'dasda', 'usuario');
INSERT INTO `8b_sp_sicobo`.`authorities` (`password`, `authority`, `enabled`, `username`) VALUES ('$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a','ROLE_USUARIO', 1, 'usuario');

INSERT INTO `8b_sp_sicobo`.`state` (`id`, `name`) VALUES(1,'Aguascalientes'),(2,'Baja California'),(3,'Baja California Sur'),(4,'Campeche'),(5,'Chiapas'),(6,'Chihuahua'),(7,'Coahuila'),(8,'Colima'),(9,'Ciudad de México'),(10,'Durango'),(11,'Estado de México'),(12,'Guanajuato'),(13,'Guerrero'),(14,'Hidalgo'),(15,'Jalisco'),(16,'Michoacán'),(17,'Morelos'),(18,'Nayarit'),(19,'Nuevo León'),(20,'Oaxaca'),(21,'Puebla'),(22,'Querétaro'),(23,'Quintana Roo'),(24,'San Luis Potosí'), (25,'Sinaloa'),(26,'Sonora'),(27,'Tabasco'),(28,'Tamaulipas'),(29,'Tlaxcala'),(30,'Veracruz'),(31,'Yucatán'),(32,'Zacatecas');

INSERT INTO `8b_sp_sicobo`.`warehouses_type` (`description`) VALUES ('Simple');
INSERT INTO `8b_sp_sicobo`.`warehouses_type` (`description`) VALUES ('Media');
INSERT INTO `8b_sp_sicobo`.`warehouses_type` (`description`) VALUES ('Especial');
INSERT INTO `8b_sp_sicobo`.`warehouses_type` (`description`) VALUES ('Empresarial');
