
CREATE TABLE EVENTS
(
  EVENT_ID           NUMBER(18)                 NOT NULL,
  ORGANIZER_USER_ID  NUMBER(10)                 NOT NULL,
  EVENT_NAME         VARCHAR2(200 CHAR)         NOT NULL,
  EVENT_TYPE         VARCHAR2(20 CHAR)          NOT NULL,
  EVENT_DATE         DATE                       NOT NULL,
  EVENT_LOCATION     VARCHAR2(200 CHAR)         NOT NULL,
  EVENT_STATUS       VARCHAR2(20 CHAR)          NOT NULL,
  EVENT_STATUS_DATE  DATE
);

COMMENT ON COLUMN EVENTS.ORGANIZER_USER_ID IS '����������� �� �������';
COMMENT ON COLUMN EVENTS.EVENT_NAME IS '������������ �� �������';
COMMENT ON COLUMN EVENTS.EVENT_TYPE IS '��� �� �������';
COMMENT ON COLUMN EVENTS.EVENT_DATE IS '���� �� ���������';
COMMENT ON COLUMN EVENTS.EVENT_LOCATION IS '����� �� ���������';
COMMENT ON COLUMN EVENTS.EVENT_STATUS IS '������ �� ���������: 0->�������; 1->��������; 2->��������;';
COMMENT ON COLUMN EVENTS.EVENT_STATUS_DATE IS '���� �� ������ �� ���������';

ALTER TABLE EVENTS ADD (CONSTRAINT EVENT_ID_PK PRIMARY KEY (EVENT_ID));
ALTER TABLE EVENTS ADD (CONSTRAINT EVENTS_ORGANIZER_USER_ID_FK FOREIGN KEY (ORGANIZER_USER_ID) REFERENCES USERS(USER_ID));

CREATE INDEX EVENTS_ORGANIZER_USER_ID_INX ON EVENTS(ORGANIZER_USER_ID);

CREATE SEQUENCE EVENTS_EVENT_ID_S�Q
START WITH 1
INCREMENT BY 1
MINVALUE 0
NOCACHE
NOCYCLE
NOORDER;

CREATE OR REPLACE TRIGGER EVENTS_ID_TR
BEFORE INSERT ON EVENTS
FOR EACH ROW
  WHEN (new.EVENT_ID IS NULL)
BEGIN
  :new.EVENT_ID := EVENTS_EVENT_ID_S�Q.NEXTVAL;
END;
