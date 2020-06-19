create table travel_code
(
	id int auto_increment,
	travel_code int not null,
	travel_code_name varchar(200) not null,
	constraint travel_code_pk
		primary key (id)
);