create schema if not exists exposed;

CREATE TABLE if not exists exposed."user"
(
    id       int generated always as identity primary key,
    login    text not null,
    password text not null,
    is_admin bool DEFAULT false
);

create schema if not exists ktorm;

CREATE TABLE if not exists ktorm."user"
(
    id       int generated always as identity primary key,
    login    text not null,
    password text not null,
    is_admin bool DEFAULT false
);