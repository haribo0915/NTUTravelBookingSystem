create table User
(
	id int auto_increment,
	name varchar(100) not null,
	account varchar(100) not null,
	password varchar(100) not null,
	constraint User_pk
		primary key (id)
);