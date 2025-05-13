
USE lodo;

DELETE FROM user_point WHERE TRUE;
DELETE FROM event_point WHERE TRUE;
DELETE FROM explorer_point WHERE TRUE;
DELETE FROM location_point WHERE TRUE;
DELETE FROM socializer_point WHERE TRUE;
DELETE FROM game WHERE TRUE;
DELETE FROM event_user WHERE TRUE;
DELETE FROM event_sport WHERE TRUE;
DELETE FROM event WHERE TRUE;
DELETE FROM location WHERE TRUE;
DELETE FROM user WHERE TRUE;
DELETE FROM sport WHERE TRUE;
DELETE FROM location_state WHERE TRUE;
DELETE FROM address WHERE TRUE;

INSERT INTO `address` (id, city, country, postcode, record_version, street, street_number) VALUES
(1,'Aarau','CH','5000',4,'Schachenallee','29'),
(2,'Altstätten','CH','9450',3,'Heidenerstrasse','5'),
(3,'Solothurn','CH','4500',6,'Brühlstrassse','120'),
(4,'weqweqw','ch','eqweqweq',1,'weqweqwe','qweqweqw'),
(5,'asa','ch','1111',1,'asd','1'),
(6,'asa','ch','1111',1,'asd','1'),
(7,'asa','ch','1111',1,'asd','1'),
(8,'asa','ch','1111',1,'asd','1'),
(9,'asa','ch','1111',1,'asd','1'),
(10,'Aarau','CH','5000',6,'Schwimmbadstrasse',''),
(11,'Aarau','CH','5000',6,'Schwimmbadstrasse',''),
(12,'Aarau','CH','5000',5,'Schwimmbadstrasse','7'),
(13,'Solothurn','CH','5000',5,'Brühlstrassse','120'),
(14,'Aarau','CH','5000',2,'Schachen','2'),
(15,'Aarau','CH','5000',3,'Schachen','2'),
(16,'Altstätten','CH','9450',3,'Sportplatz','2'),
(17,'Altstätten','CH','9450',3,'Kriessernstrasse',''),
(18,'Widnau','CH','9443',3,'Aegetenstrasse','60'),
(23,'Solothurn','CH','4500',1,'street','2');

INSERT INTO `location_state` (id, name, record_version) VALUES
(1,'valid',1),
(2,'invalid',1),
(3,'to verify',1);

INSERT INTO `sport` VALUES
(1,'fas-basketball-ball','Basketball',1,'none'),
(2,'fas-futbol','Fussball',1,'none2'),
(3,'fas-chess','Schach',1,'non3'),
(4,'fas-volleyball-ball','Volleyball',1,'none4'),
(5,'fas-table-tennis','Tischtennis',1,'Der Spieler der zuerst 11 Punkte erzielt, hat das Spiel gewonnen. Haben beide Spieler 10 Punkte erreicht, geht es in die Verlängerung. Es gewinnt derjenige den Satz, der zuerst mit 2 Punkten Vorsprung führt (Beispiel: 12:10, 13:11 etc.).');

INSERT INTO `user` 
(id, authenticated, blocked, count_login, count_password_reset, display_image, display_name, first_name, is_admin, is_verified, last_name, mobile_number, password_hash, receive_price_per_post, record_version, register_datetime, user_name, user_type,  address_id) VALUES
(1,0,0,0,0,NULL,'','Paula',NULL,NULL,'Beck','0791234567','ziizuiopozippz',0,1,'2019-11-16 00:00:00.000','Paula','PLAYER',5),
(2,0,0,0,0,NULL,'','Peter',NULL,NULL,'Green','0191234568','ziizuiopozippz',0,1,'2019-11-16 00:00:00.000','Peter','PLAYER',6),
(3,1,0,0,0,NULL,'','Philip',NULL,NULL,'Black','0791111111','$2a$10$6mMWYXOJUsfzmWfg6Dx4euwb78KnnHmjiXXNLC6gq2zt5c5wHHigC',0,1,'2019-11-16 00:00:00.000','Phil','PLAYER',7),
(4,1,0,0,0,NULL,'','Andrey',NULL,NULL,'Black','0766666666','$2a$10$4eC7vU.ZE53dPXBBklg93eC3YzEbdd2DFzDUN0NEyDo02jbU.NNwa',0,1,'2019-11-16 00:00:00.000','Andrey','PLAYER',8),
(5,1,0,0,0,NULL,'','Wojtek',NULL,NULL,'Red','0762222222','$2a$10$YywH7GefWSLZ0ZP/h4zJkOdicYZwfgPtHXQoYweN8Eslqrg85WKfu',0,1,'2019-11-16 00:00:00.000','Wojtek','PLAYER',9),
(6,1,0,0,0,NULL,'Admin','aadmin',1,1,'aaadmin',NULL,'$2a$10$9suK3RmdVMpwd1RQ1h.4c.jbwEVsTEN655nyTYfPjco7DLzy7qx/O',0,0,'2019-11-16 00:00:00.000','admin','PORTAL_USER',1);

INSERT INTO `location` 
(id, creation_datetime, latitude, longitude, `name`, record_version, verification_datetime, address_id, player_id, location_state_id, sport_id) VALUES
(1,'2019-11-16 00:00:00.000',45.794,15.846,'Sporthalle Schachen',2,NULL,1,1,1,1),
(2,'2019-11-16 00:00:00.000',45.799,15.873,'Schulhaus Feld',2,NULL,2,2,2,5),
(3,'2019-11-16 00:00:00.000',45.797,15.858,'Schulhaus Brühl',4,NULL,3,3,3,3),
(4,'2019-11-16 00:00:00.000',45.796,15.999,'Freibad Schachen',2,NULL,10,3,1,5),
(5,'2019-11-16 00:00:00.000',45.771,15.886,'Leichtathletikstadion Schachen',2,NULL,11,3,1,2),
(6,'2019-11-16 00:00:00.000',45.797,15.858,'Sportanlage Schachen',2,NULL,12,3,2,2),
(7,'2019-11-16 00:00:00.000',45.699,15.903,'Schulhaus Brühl',2,NULL,13,3,1,1),
(8,'2020-02-20 00:00:00.000',47.383971, 9.547377,'GESA',2,NULL,16,3,1,2),
(9,'2020-02-20 00:00:00.000',47.374386, 9.561904,'Sportanlage Grüntal',2,NULL,17,3,1,3),
(10,'2020-02-20 00:00:00.000',47.394490, 9.639127,'Aegeten',2,NULL,18,3,1,4);

INSERT INTO `event`
(id, creation_datetime, end_datetime, latitude, longitude, name, record_version, registration_code, sponsor_text, start_datetime, creator_id, responsible_user_id, address_id) VALUES
(1,'2020-01-11 19:58:02.458','2020-05-10 08:00:00.000',33,22,'Schachen Aarau',20,'xxx','','2020-05-10 05:00:00.000',6,6,14),
(2,'2020-01-12 10:19:39.170','2020-06-06 00:00:00.000',2,2,'Schachen Aarau',4,'xxxx','','2020-06-06 00:00:00.000',6,6,15),
(3,'2020-02-20 13:02:34.693','2020-02-24 19:00:00.000',47.390930, 9.589078,'Tennisturnier',2,'tennis','','2020-02-28 08:00:00.000',6,6,16),
(10,'2020-01-12 13:02:34.693','2020-06-21 00:00:00.000',0,0,'Soloturn',2,'xxx','','2020-06-20 00:00:00.000',6,6,23);

INSERT INTO `event_sport`
(id, record_version, event_id, sport_id)
VALUES (1,1,1,2),(17,1,2,1),(20,1,1,1),(21,1,1,5),(22,1,2,5),(23,1,2,2),(24,1,10,1),(25,1,10,5),(26,1,10,2),(27,1,3,5);

INSERT INTO `game` (
id, accept_datetime, appointment_1_datetime, appointment_2_datetime, appointment_3_datetime, count_for_leader_board, creation_datetime, execution_datetime, has_host_made_appointments, record_version, result_record_datetime, verified_datetime, guest_player_id, host_player_id, location_id, event_sport_id, guest_score, host_score, game_state)
VALUES
(1,'2019-11-16 00:00:00.000','2019-11-16 00:00:00.000','2019-11-16 00:00:00.000','2019-11-16 00:00:00.000',0,'2019-11-16 00:00:00.000','2019-11-20 00:00:00.000',0,4,'2019-11-16 00:00:00.000','2019-11-16 00:00:00.000',1,2,1,NULL,8,5,'PLAYED'),
(2,'2019-11-17 00:00:00.000','2019-11-17 00:00:00.000','2019-11-17 00:00:00.000','2019-11-17 00:00:00.000',0,'2019-11-17 00:00:00.000','2019-11-18 00:00:00.000',0,10,'2019-11-17 00:00:00.000','2019-11-17 00:00:00.000',1,2,4,NULL,12,6,'RECORDED'),
(3,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',1,'2019-10-17 00:00:00.000','2019-12-25 00:00:00.000',0,4,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',5,3,2,NULL,15,14,'PLAYED'),
(4,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',1,'2019-10-17 00:00:00.000','2019-12-26 00:00:00.000',0,3,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',NULL,3,2,NULL,NULL,NULL,'OPEN'),
(5,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',1,'2019-10-17 00:00:00.000','2019-12-26 00:00:00.000',0,7,'2019-10-17 00:00:00.000','2019-10-17 00:00:00.000',5,3,2,NULL,NULL,NULL,'OPEN'),
('18', NULL, NULL, NULL, NULL, '1', '2019-12-28 01:37:23.396', '2019-12-28 01:37:23.589', '1', '1', '2019-12-28 01:37:23.589', NULL, '4', '1', NULL, '24', '6', '5', 'RECORDED'),
('19', NULL, NULL, NULL, NULL, '1', '2019-12-28 01:37:23.396', '2019-12-28 01:37:23.589', '1', '1', '2019-12-28 01:37:23.589', NULL, '4', '1', NULL, '25', '6', '5', 'RECORDED'),
('20', NULL, NULL, NULL, NULL, '1', '2019-12-28 01:37:23.396', '2019-12-28 01:37:23.589', '1', '1', '2019-12-28 01:37:23.589', NULL, '4', '1', NULL, '26', '6', '5', 'RECORDED')
;


INSERT INTO `location_point` 
(id, points, record_version, location_id, user_id, lost_games_count, win_games_count, opponent_id)
VALUES (1,12,1,1,1,0,1,2),(2,6,1,1,5,0,2,3),(3,100,1,2,1,0,3,4),(4,12,1,5,1,0,1,2),(5,6,1,5,5,0,2,3),(6,100,1,5,1,0,3,4);


INSERT INTO `event_point` 
(id, points, record_version, event_sport_id, user_id, lost_games_count, win_games_count, opponent_id) 
VALUES (1,15,1,24,1,1,4,5),(2,22,1,24,5,2,3,5),(3,7,1,24,1,3,2,5),(4,14,1,25,2,4,1,5);

INSERT INTO `user_point`
(id, user_id, sport_id, points)
VALUES (1,1,1,88), (2,1,2,66), (3,1,2,45), (4,5,1,36);