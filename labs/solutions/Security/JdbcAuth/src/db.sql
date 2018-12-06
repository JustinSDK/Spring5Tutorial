CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);

CREATE TABLE t_account (
  name VARCHAR(15) NOT NULL,
  password VARCHAR(64) NOT NULL,
  enabled TINYINT NOT NULL,
  PRIMARY KEY (name)
);

CREATE TABLE t_account_role (
    name VARCHAR(15) NOT NULL,
    role VARCHAR(15) NOT NULL,
    PRIMARY KEY (name, role)
);

INSERT INTO t_account(name, password, enabled) VALUES ('admin','$2a$10$PUFa4u8d434aWitf87scE.vue580tghpCU6JdPnDXQgjK1q0Ddtgu', 1);
INSERT INTO t_account(name, password, enabled) VALUES ('caterpillar','$2a$10$yh5WJetawp2KloUtEoVzRuT4/WEeR5BhPdfRZGoAvnCtKAbFBP8Sa', 1);
INSERT INTO t_account_role (name, role) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO t_account_role (name, role) VALUES ('admin', 'ROLE_MEMBER');
INSERT INTO t_account_role (name, role) VALUES ('caterpillar', 'ROLE_MEMBER');