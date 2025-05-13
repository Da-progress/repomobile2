ALTER TABLE `game` 
ADD COLUMN `has_host_suggested_result` TINYINT(1) NOT NULL DEFAULT '1' AFTER `event_sport_id`;

