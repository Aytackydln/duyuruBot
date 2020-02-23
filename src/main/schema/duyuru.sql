/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE IF NOT EXISTS `duyuru` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `duyuru`;

CREATE TABLE IF NOT EXISTS `Announcement`
(
    `topic_id` varchar(100) NOT NULL,
    `title`    varchar(152) NOT NULL DEFAULT '',
    `link`     varchar(152) NOT NULL,
    `date`     datetime     NOT NULL,
    PRIMARY KEY (`topic_id`, `title`, `link`),
    KEY `FK_announcement_topic` (`topic_id`),
    KEY `i_announcement_date` (`date`) USING BTREE,
    CONSTRAINT `FK_announcement_topic` FOREIGN KEY (`topic_id`) REFERENCES `Topic` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Announcement`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `Announcement`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `authorities`
(
    `username`  varchar(50) NOT NULL,
    `authority` varchar(50) NOT NULL,
    PRIMARY KEY (`username`, `authority`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

/*!40000 ALTER TABLE `authorities`
    DISABLE KEYS */;
INSERT INTO `authorities` (`username`, `authority`)
VALUES ('admin', 'ROLE_USER');
/*!40000 ALTER TABLE `authorities`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `Configuration`
(
    `property` varchar(30) NOT NULL,
    `value`    text        NOT NULL,
    PRIMARY KEY (`property`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Configuration`
    DISABLE KEYS */;
INSERT INTO `Configuration` (`property`, `value`)
VALUES ('cleanAnnouncements', 'false'),
       ('defaultLanguage', 'en'),
       ('webhookUrl', 'https://cengduyuru.tk/');
/*!40000 ALTER TABLE `Configuration`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `Department`
(
    `topic_id`             varchar(100) NOT NULL DEFAULT '',
    `classesUri`           text,
    `classElementSelector` text,
    PRIMARY KEY (`topic_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Department`
    DISABLE KEYS */;
INSERT INTO `Department` (`topic_id`, `classesUri`, `classElementSelector`)
VALUES ('CENG', 'Dersler.aspx', '#ContentPlaceHolder1_tblDersler>tbody a');
/*!40000 ALTER TABLE `Department`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `logins`
(
    `username` tinytext NOT NULL,
    `password` text,
    `enabled`  bit(1) DEFAULT b'1',
    PRIMARY KEY (`username`(50))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `logins`
    DISABLE KEYS */;
INSERT INTO `logins` (`username`, `password`, `enabled`)
VALUES ('admin', '$2a$10$MPoKdfDcEgex6RAUnC.x0eV/wzJIVYqy80Gtfh9BQC.xo578n.YFe', b'1');
/*!40000 ALTER TABLE `logins`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `Message`
(
    `user_id` int(52)             DEFAULT NULL,
    `id`      bigint(32) NOT NULL AUTO_INCREMENT,
    `time`    datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `text`    mediumtext CHARACTER SET latin5 COLLATE latin5_turkish_ci,
    PRIMARY KEY (`id`),
    KEY `FK_message_user` (`user_id`),
    CONSTRAINT `FK_message_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Message`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `Message`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `Subscription`
(
    `user_id`  int(52)      NOT NULL,
    `topic_id` varchar(100) NOT NULL,
    PRIMARY KEY (`user_id`, `topic_id`),
    KEY `FK_subscription_topic` (`topic_id`),
    KEY `i_subscription_user` (`user_id`),
    CONSTRAINT `FK_subscription_topic` FOREIGN KEY (`topic_id`) REFERENCES `Topic` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_subscription_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Subscription`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `Subscription`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `Topic`
(
    `id`          varchar(100) NOT NULL,
    `type`        enum ('CENG','CENG_CLASS','MF') DEFAULT NULL,
    `baseLink`    mediumtext,
    `boardAppend` mediumtext,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Topic`
    DISABLE KEYS */;
INSERT INTO `Topic` (`id`, `type`, `baseLink`, `boardAppend`)
VALUES ('CENG', 'CENG', 'http://ceng.eskisehir.edu.tr/', 'Duyurular.aspx'),
       ('Mühendislik Fakültesi', 'MF', 'https://mf.eskisehir.edu.tr/', 'duyuru');
/*!40000 ALTER TABLE `Topic`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `Translation`
(
    `language` varchar(5) NOT NULL,
    `sentence` text       NOT NULL,
    `text`     text       NOT NULL,
    PRIMARY KEY (`language`, `sentence`(100))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `Translation`
    DISABLE KEYS */;
INSERT INTO `Translation` (`language`, `sentence`, `text`)
VALUES ('en', 'CANCEL', 'Cancel'),
       ('en', 'REQUEST_ERROR', 'there was an error processing your request'),
       ('en', 'SUBSCRIBE_FAIL', 'you could not subscribe'),
       ('en', 'SUBSCRIBE_LIST_HEADER', 'Choose the topic to subscribe'),
       ('en', 'SUBSCRIBE_SUCCESS', 'you are now subscribed'),
       ('en', 'SUBSCRIBED_LIST_HEADER', 'Topics you subscribed:'),
       ('en', 'SUBSCRIPTIONS_EMPTY', 'You dont have any subscriptons'),
       ('en', 'TOPIC_NOT_EXISTS',
        'topic does not exists orcould not be parsed because of its name. You will be subscribed manually later.'),
       ('en', 'UNAVAILABLE_APOLOGY', 'Sorry, i was offline when you sent me command. I can accept new commands soon.'),
       ('en', 'UNSUBSCRIBE_FAIL', 'you could not unsubscribe'),
       ('en', 'UNSUBSCRIBE_LIST_HEADER', 'Choose the topic to unsubscribe'),
       ('en', 'UNSUBSCRIBE_SUCCESS', 'you are now unsubscribed'),
       ('en', 'WELCOME',
        'Hello, you can send me commands by pressing the  /  button below.  Example commands are:\r\n/list - shows your subscriptions\r\n/subscribe - opens menu to subscribe topics\r\n/unsubscribe - removes selected subscription\r\nNote: If you are the first subscriber of a topic you will get notifications of all the announcements of that topic for one time only.'),
       ('tr', 'CANCEL', 'İptal'),
       ('tr', 'REQUEST_ERROR', 'isteğini işlemede hata oluştu'),
       ('tr', 'SUBSCRIBE_FAIL', 'abone olamadın'),
       ('tr', 'SUBSCRIBE_LIST_HEADER', 'Abone olacağın başlığı seç:'),
       ('tr', 'SUBSCRIBE_SUCCESS', 'artık abonesin'),
       ('tr', 'SUBSCRIBED_LIST_HEADER', 'Abone olduğun başlıklar:'),
       ('tr', 'SUBSCRIPTIONS_EMPTY', 'Hiçbir konuya abone değilsin'),
       ('tr', 'TOPIC_NOT_EXISTS',
        'gerçekte olmayan veya ismi işlenmesinde hata olan bir başlık. Daha sonradan elle abone edileceksiniz.'),
       ('tr', 'UNAVAILABLE_APOLOGY',
        'Üzgünüm, komutlarını gönderdiğinde kapalıydım. Yeni mesajlarını birazdan alabilirim'),
       ('tr', 'UNSUBSCRIBE_FAIL', 'aboneliğini kaldıramadın'),
       ('tr', 'UNSUBSCRIBE_LIST_HEADER', 'Aboneliğini sileceğin başlığı seç:'),
       ('tr', 'UNSUBSCRIBE_SUCCESS', 'aboneliğini kaldırdın'),
       ('tr', 'WELCOME',
        'Hoş geldin. Aşağıdaki  /  tuşuna basarak bana komut gönderebilirsin. Örnek komutlar\r\n/list - aboneliklerini gösterir\r\n/subscribe - abonelik seçim menüsü\r\n/unsubscribe - abonelik kaldırır\r\nNot: Bir derse ilk abone olan sizseniz o dersin mevcut duyuruların tamamı bir kereliğine bildirim olarak gelecektir');
/*!40000 ALTER TABLE `Translation`
    ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `User`
(
    `id`        int(52) NOT NULL,
    `username`  tinytext,
    `name`      tinytext,
    `last_name` tinytext,
    `language`  varchar(5) DEFAULT 'en',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

/*!40000 ALTER TABLE `User`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `User`
    ENABLE KEYS */;

/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
