INSERT INTO `sicobo`.`users` (`email`, `lastname`, `name`, `number_attempts`, `password`, `phone_number`, `policy_acceptance`, `rfc`, `role`, `enabled`, `surname`, `username`) VALUES ('hola@gmail.com', 'dasdas', 'gfgf', '0', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '7775996014', '1', '1', 'ROLE_ADMIN', '1', 'dasda', 'admin');
INSERT INTO `sicobo`.`authorities` (`password`, `authority`, `enabled`, `username`) VALUES ('$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a','ROLE_ADMIN', 1, 'admin');


INSERT INTO `sicobo`.`users` (`email`, `lastname`, `name`, `number_attempts`, `password`, `phone_number`, `policy_acceptance`, `rfc`, `role`, `enabled`, `surname`, `username`) VALUES ('hola@gmail.com', 'dasdas', 'gfgf', '0', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '7775996014', '1', '1', 'ROLE_GESTOR', '1', 'dasda', 'gestor');
INSERT INTO `sicobo`.`authorities` (`password`, `authority`, `enabled`, `username`) VALUES ('$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a','ROLE_GESTOR', 1, 'gestor');

INSERT INTO `sicobo`.`users` (`email`, `lastname`, `name`, `number_attempts`, `password`, `phone_number`, `policy_acceptance`, `rfc`, `role`, `enabled`, `surname`, `username`) VALUES ('hola@gmail.com', 'dasdas', 'gfgf', '0', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '7775996014', '1', '1', 'ROLE_USUARIO', '1', 'dasda', 'usuario');
INSERT INTO `sicobo`.`authorities` (`password`, `authority`, `enabled`, `username`) VALUES ('$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a','ROLE_USUARIO', 1, 'usuario');
