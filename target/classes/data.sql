--DROP TABLE USER;
--
--CREATE TABLE USER (
--id integer,
--username varchar(32),
--password varchar(255),
--savings_balance float,
--checking_balance float
--);
INSERT INTO USER (id, username, password, savings_balance, checking_balance) values(
1,
'ben',
'$2a$12$jJw51q5MH500N92zk5iaj.1ZUlyBX7l729quB/suRgM9DruZkLfUK',
4000.00,
350.00
);
--CREATE TABLE TRANSACTION (
--  username varchar(32),
--  account_into varchar(32),
--  account_from  varchar(32),
--  id integer
-- );