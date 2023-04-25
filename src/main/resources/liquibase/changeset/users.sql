-- liquibase formatted sql

-- changeset garry:create_users
CREATE TABLE users
(
    id         SERIAL NOT NULL PRIMARY KEY ,
    first_name VARCHAR(255) NOT NULL ,
    last_name  VARCHAR(255) NOT NULL ,
    username   varchar(255) NOT NULL UNIQUE ,
    password   VARCHAR(255) NOT NULL ,
    phone      VARCHAR(255) ,
    role       VARCHAR(255) ,
    enabled    BOOLEAN NOT NULL,
    image      bytea
);