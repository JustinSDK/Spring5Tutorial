CREATE TABLE message (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(15) NOT NULL,
    millis BIGINT NOT NULL,
    blabla VARCHAR(512) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO message(username, millis, blabla) VALUES('caterpillar', 1518666695521, '我是一隻弱小的毛毛蟲&#xff0c;想像有天可以成為強壯的挖土機&#xff0c;擁有挖掘夢想的神奇手套&#xff01;');
INSERT INTO message(username, millis, blabla) VALUES('caterpillar', 1518666716781, '碁峰把《Java SE 9 技術手冊》電子書放上去囉&#xff01;');
INSERT INTO message(username, millis, blabla) VALUES('caterpillar', 1518666769369, 'JavaScript 名稱空間管理 https://openhome.cc/Gossip/ECMAScript/NameSpace.html');
