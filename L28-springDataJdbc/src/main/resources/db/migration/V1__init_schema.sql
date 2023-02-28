create table client
(
    id         bigserial not null primary key,
    first_name varchar(50),
    last_name  varchar(50)
);

create table address
(
    client_id      bigint       not null references client (id),
    client_address varchar(300) not null
);

create table phone
(
    id        bigserial   not null primary key,
    number    varchar(30) not null,
    client_id bigint      not null references client (id)
);
