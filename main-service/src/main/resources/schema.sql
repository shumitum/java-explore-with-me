drop table if exists users;
drop table if exists categories;
drop table if exists events;

create table if not exists users
(
    user_id bigint generated by default as identity,
    name    varchar(250) not null,
    email   varchar(254) not null,
    constraint users_pk primary key (user_id)
);

create table if not exists categories
(
    category_id bigint generated by default as identity,
    name        varchar(50) not null,
    constraint categories_pk primary key (category_id)
);

create table if not exists events
(
    event_id bigint generated by default as identity

/*    name        varchar(50) not null,
    constraint event_pk primary key (event_id)*/
);