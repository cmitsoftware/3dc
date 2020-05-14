ALTER TABLE `gym`.`person` 
ADD COLUMN `first_registration_date` DATETIME NULL AFTER `number`,
ADD COLUMN `approval_date` DATETIME NULL AFTER `first_registration_date`;
