insert into client (first_name, last_name)
values ('Ivan', 'Ivanon');

insert into address (client_id, client_address)
values (1, 'Moscow');

insert into phone (number, client_id)
values ('33-24-23', 1), ('22-02-01', 1);


insert into client (first_name, last_name)
values ('Petr', 'Petrov');

insert into address (client_id, client_address)
values (2, 'Lipetsk');

insert into phone (number, client_id)
values ('55-55-55', 2), ('11-11-01', 2);