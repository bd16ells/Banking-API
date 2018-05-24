--DROP TABLE USER;
--
--CREATE TABLE USER (
--id integer,
--username varchar(32),
--password varchar(255),
--savings_balance float,
--checking_balance float
--);

INSERT INTO USER (id, username, password) values(
1,
'ben',
'$2a$12$jJw51q5MH500N92zk5iaj.1ZUlyBX7l729quB/suRgM9DruZkLfUK'
);

ALTER TABLE ACCOUNT
DROP PRIMARY KEY;

ALTER TABLE ACCOUNT
ADD PRIMARY KEY(id);

INSERT INTO ACCOUNT(id, balance, type, name, user_id) values(
1,
4500.00,
0,
'Savings_Main_share',
1
);
INSERT INTO ACCOUNT(id, balance, type, name, user_id) values(
2,
350.00,
1,
'Primary_Checking',
1
);
INSERT INTO ACCOUNT(id, balance, type, name, user_id) values(
3,
2240.00,
0,
'Savings_Vacation',
1
);