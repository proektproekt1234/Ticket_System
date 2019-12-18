
CREATE TABLE TICKETS
(
  TICKET_ID            NUMBER(24)               NOT NULL,
  EVENT_ID             NUMBER(18)               NOT NULL,
  SEATS_ID             NUMBER(18)               NOT NULL,
  DISTRIBUTOR_USER_ID  NUMBER(10)               NOT NULL,
  CUSTOMER_NAME        VARCHAR2(32 CHAR)        NOT NULL,
  NUMBER_OF_TICKETS    NUMBER(4)                NOT NULL,
  SEAT_NUMBER          NUMBER(6)                NOT NULL
);

COMMENT ON COLUMN TICKETS.DISTRIBUTOR_USER_ID IS '����������� �� ������';
COMMENT ON COLUMN TICKETS.CUSTOMER_NAME IS '����� �� ��������';
COMMENT ON COLUMN TICKETS.NUMBER_OF_TICKETS IS '���� ��������� ������';
COMMENT ON COLUMN TICKETS.SEAT_NUMBER IS '����� �� �������';

ALTER TABLE TICKETS ADD (CONSTRAINT TICKET_ID_PK PRIMARY KEY (TICKET_ID));
ALTER TABLE TICKETS ADD (CONSTRAINT TICKETS_EVENT_ID_FK FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(EVENT_ID));
ALTER TABLE TICKETS ADD (CONSTRAINT TICKETS_SEATS_ID_FK FOREIGN KEY (SEATS_ID) REFERENCES SEATS(SEATS_ID));
ALTER TABLE TICKETS ADD (CONSTRAINT TICKETS_DISTRIBUTOR_FK FOREIGN KEY (DISTRIBUTOR_USER_ID) REFERENCES USERS(USER_ID));

CREATE INDEX TICKETS_EVENT_ID_INX ON TICKETS(EVENT_ID);
CREATE INDEX TICKETS_SEATS_ID_INX ON TICKETS(SEATS_ID);
CREATE INDEX TICKETS_DISTRIBUTOR_INX ON TICKETS(DISTRIBUTOR_USER_ID);

CREATE SEQUENCE TICKETS_TICKET_ID_SEQ
START WITH 1
INCREMENT BY 1
MINVALUE 0
NOCACHE
NOCYCLE
NOORDER;
