CREATE TABLE `member` (
  `member_id` varchar(30) NOT NULL,
  `member_password` varchar(100) NOT NULL,
  `member_name` varchar(30) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `rolename` varchar(20) DEFAULT 'ROLE_USER',
  PRIMARY KEY (`member_id`),
  CONSTRAINT `member_chk_1` CHECK ((`enabled` in (0,1))),
  CONSTRAINT `member_chk_2` CHECK ((`rolename` in (_utf8mb4'ROLE_USER',_utf8mb4'ROLE_ADMIN')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `web5_board` (
  `board_num` int NOT NULL AUTO_INCREMENT,
  `board_title` varchar(200) NOT NULL,
  `board_content` varchar(255) NOT NULL,
  `hits` int DEFAULT '0',
  `created_date` timestamp NULL DEFAULT NULL,
  `member_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`board_num`),
  KEY `fk_board_member` (`member_id`),
  CONSTRAINT `fk_board_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL,
  `board_num` int NOT NULL,
  `member_id` varchar(30) NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `board_num` (`board_num`),
  KEY `member_id` (`member_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`board_num`) REFERENCES `web5_board` (`board_num`),
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
