create table admin
(
    id   bigint not null primary key,
    login varchar(50) unique,
    password varchar(50)
);