CREATE TABLE account (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(15) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password VARCHAR(64) NOT NULL,
    enabled TINYINT NOT NULL,
    role VARCHAR(15) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE message (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(15) NOT NULL,
    millis BIGINT NOT NULL,
    blabla VARCHAR(512) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO account(name, email, password, enabled, role) VALUES ('admin','admin@openhome.cc','$2a$10$PUFa4u8d434aWitf87scE.vue580tghpCU6JdPnDXQgjK1q0Ddtgu', 1, 'ROLE_MEMBER');
INSERT INTO account(name, email, password, enabled, role) VALUES ('caterpillar','caterpillar@openhome.cc','$2a$10$yh5WJetawp2KloUtEoVzRuT4/WEeR5BhPdfRZGoAvnCtKAbFBP8Sa', 0, 'ROLE_MEMBER');

INSERT INTO message(username, millis, blabla) VALUES('caterpillar', 1518666695521, '我是一隻弱小的毛毛蟲&#xff0c;想像有天可以成為強壯的挖土機&#xff0c;擁有挖掘夢想的神奇手套&#xff01;');
INSERT INTO message(username, millis, blabla) VALUES('caterpillar', 1518666716781, '碁峰把《Java SE 9 技術手冊》電子書放上去囉&#xff01;');
INSERT INTO message(username, millis, blabla) VALUES('caterpillar', 1518666769369, 'JavaScript 名稱空間管理 https://openhome.cc/Gossip/ECMAScript/NameSpace.html');
