-- liquibase formatted sql

-- changeset garry:create_ad
CREATE TABLE ad
(
    pk          SERIAL PRIMARY KEY ,
    title       VARCHAR(255) NOT NULL ,
    description VARCHAR(255) NOT NULL ,
    price       INTEGER      NOT NULL ,
    author_id   INTEGER REFERENCES users ,
    image       bytea
);