alter table client
    add address_id bigint;

create table address
(
    address_id     bigint       not null primary key,
    client_address varchar(300) not null
);

alter table client
    add constraint fk_client_address foreign key (address_id) references address;

