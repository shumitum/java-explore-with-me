drop table if exists requests;
drop table if exists events;
drop table if exists categories;
drop table if exists users;

create table if not exists users
(
    user_id bigint generated by default as identity,
    name    varchar(250) unique not null,
    email   varchar(254)        not null,
    constraint users_pk primary key (user_id)
);

create table if not exists categories
(
    category_id bigint generated by default as identity,
    name        varchar(50) unique not null,
    constraint categories_pk primary key (category_id)
);

create table if not exists events
(
    event_id           bigint generated by default as identity,
    annotation         varchar(2000) not null,
    category_id        bigint        not null,
    created_on         timestamp     not null,
    confirmed_requests bigint,
    description        varchar(7000) not null,
    event_date         timestamp     not null,
    initiator_id       bigint        not null,
    lat                float         not null,
    lon                float         not null,
    paid               boolean       not null,
    participant_limit  integer       not null,
    published_on       timestamp,
    request_moderation boolean       not null,
    state              varchar(50)   not null,
    title              varchar(120)  not null,
    views              bigint,
    constraint events_pk primary key (event_id),
    constraint fk_events_to_users foreign key (initiator_id) references users (user_id) on delete cascade,
    constraint fk_events_to_categories foreign key (category_id) references categories (category_id) on delete cascade
);

create table if not exists requests
(
    request_id   bigint generated by default as identity,
    event_id     bigint      not null,
    requester_id bigint      not null,
    status       varchar(50) not null,
    created      timestamp   not null,
    constraint requests_pk primary key (request_id),
    constraint event_requester_unique unique (event_id, requester_id),
    constraint fk_requests_to_events foreign key (event_id) references events (event_id) on delete cascade,
    constraint fk_requests_to_users foreign key (requester_id) references users (user_id) on delete cascade
);