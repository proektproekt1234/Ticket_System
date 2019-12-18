
CREATE TABLE USERS
(
  USER_ID        NUMBER(10)                     NOT NULL,
  USER_NAMES     VARCHAR2(120 CHAR)             NOT NULL,
  LOGIN_NAME     VARCHAR2(20 CHAR)              NOT NULL,
  USER_PASS      VARCHAR2(32 CHAR)              NOT NULL,
  USER_RATING    NUMBER(12),
  CREATION_DATE  DATE                           NOT NULL,
  STATUS         VARCHAR2(2 CHAR)               NOT NULL,
  STATUS_DATE    DATE                           NOT NULL,
  USER_ROLE      VARCHAR2(2 CHAR)               NOT NULL,
  NOTE           VARCHAR2(200 CHAR)
);

COMMENT ON COLUMN USERS.USER_NAMES IS 'Пълните имена на потребителя';
COMMENT ON COLUMN USERS.LOGIN_NAME IS 'Потребителско име в системата';
COMMENT ON COLUMN USERS.USER_PASS IS 'Парола на потребителя';
COMMENT ON COLUMN USERS.CREATION_DATE IS 'Дата на създаване на потребителя';
COMMENT ON COLUMN USERS.USER_ROLE IS '0 -> Администратор; 1 -> Организатор; 2 -> Разпространител)';
COMMENT ON COLUMN USERS.STATUS IS 'Статус на потребителя -> 00=Активен; 01=Изисква смяна на паролата; 02=Деактивиран;';
COMMENT ON COLUMN USERS.STATUS_DATE IS 'Статус от дата';
COMMENT ON COLUMN USERS.NOTE IS 'Забележки за потребителя';

ALTER TABLE USERS ADD (CONSTRAINT USER_ID_PK PRIMARY KEY (USER_ID));

ALTER TABLE USERS ADD CONSTRAINT LOGIN_NAME_UNIQUE UNIQUE(LOGIN_NAME);

CREATE SEQUENCE USERS_USER_ID_SEQ
START WITH 1
INCREMENT BY 1
MINVALUE 0
NOCACHE
NOCYCLE
NOORDER;

INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Администратор', 'Admin', '123', null, sysdate, '00', sysdate, '0', 'Built-in administrator account');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Иван Стоянов', 'ivan', '123', null, sysdate, '00', sysdate, '1', 'Организатор на събития');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Пламен Петров', 'plamen', '123', null, sysdate, '00', sysdate, '1', 'Организатор на събития');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Стефан Стефанов', 'stefan', '123', null, sysdate, '00', sysdate, '2', 'Дистрибутор');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Росен Иванов', 'rosen', '123', null, sysdate, '00', sysdate, '2', 'Дистрибутор');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Мария Петрова', 'maria', '123', null, sysdate, '00', sysdate, '2', 'Дистрибутор');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Силвия Тодорова', 'silvia', '123', null, sysdate, '00', sysdate, '2', 'Дистрибутор');
INSERT INTO USERS VALUES (USERS_USER_ID_SEQ.NEXTVAL, 'Кирил Господинов', 'kiril', '123', null, sysdate, '00', sysdate, '2', 'Дистрибутор');
COMMIT;

CREATE OR REPLACE TRIGGER USERS_ID_TR
BEFORE INSERT ON USERS
FOR EACH ROW
  WHEN (new.USER_ID IS NULL)
BEGIN
  :new.USER_ID := USERS_USER_ID_SEQ.NEXTVAL;
END;
