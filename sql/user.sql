create table User
(
	id int auto_increment,
	account varchar(100) not null,
	password varchar(100) not null,
	constraint User_pk
		primary key (id)
);