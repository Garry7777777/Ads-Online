-- liquibase formatted sql

-- changeset garry:create_comment
CREATE TABLE comment
(
    pk         SERIAL NOT NULL PRIMARY KEY ,
    created    TIMESTAMP,
    text       VARCHAR(255),
    author_id  INTEGER REFERENCES users,
    ad_pk      INTEGER REFERENCES ad
);