CREATE TABLE USERS
(
    ID_USER  number primary key,
    LOGIN    varchar2(16) not null unique,
    PASSWORD varchar2(16) not null,
    NAME     varchar2(16) not null
);

CREATE SEQUENCE SQ_USERS START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

CREATE
    OR REPLACE TRIGGER TR_PK_USERS before
    INSERT
    ON USERS FOR EACH ROW BEGIN
    SELECT
        SQ_USERS.NEXTVAL INTO :NEW.ID_USER
    FROM
        dual;
END;

CREATE TABLE MESSAGES
(
    ID_MESSAGE       number primary key,
    QUOTE_MESSAGE_ID number references MESSAGES (ID_MESSAGE),
    CONTENT          varchar2(200) not null,
    SENDER_ID        number        not null references USERS (ID_USER),
    RECEIVER_ID      number        not null references USERS (ID_USER),
    STATUS_UNREAD    number(1)     not null,
    STATUS_EDITED    number(1)     not null,
    TIME             date          not null
);

CREATE SEQUENCE SQ_MESSAGES START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

CREATE
    OR REPLACE TRIGGER TR_PK_MESSAGES before
    INSERT
    ON MESSAGES FOR EACH ROW BEGIN
    SELECT
        SQ_MESSAGES.NEXTVAL INTO :NEW.ID_MESSAGE
    FROM
        dual;
END;