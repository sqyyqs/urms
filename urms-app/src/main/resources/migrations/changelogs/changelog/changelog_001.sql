-- liquibase formatted sql

-- changeset demin.k:001-1
create table t_user
(
    id       bigserial primary key,
    login    varchar(255) not null unique,
    password text         not null,
    name     varchar(255) not null
);

-- changeset demin.k:001-2
create table request
(
    id             bigserial primary key,
    request_status varchar(255)                  not null default 'DRAFT' check (request_status IN ('DRAFT', 'SENT', 'ACCEPTED', 'REJECTED')),
    c_text         text,
    phone_number   varchar(255),
    created_at     timestamp                     not null default current_timestamp,
    user_id        bigint references t_user (id) not null,
    name           varchar(255)
);

-- changeset demin.k:001-3
create table user_authority
(
    id        bigserial primary key,
    user_id   bigint references t_user (id) on delete cascade not null,
    authority varchar(255)                                    not null check (authority IN ('USER', 'OPERATOR', 'ADMINISTRATOR'))
);

-- changeset demin.k:001-4
create table black_list_token
(
    id    bigserial primary key,
    token text not null
);
