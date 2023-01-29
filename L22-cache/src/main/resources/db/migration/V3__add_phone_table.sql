create table phone
(
    phone_id  bigint      not null primary key,
    number    varchar(11) not null,
    client_id bigint references client
);