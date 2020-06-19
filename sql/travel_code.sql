create table travel_code
(
	travel_code int not null UNIQUE,
	travel_code_name varchar(200) not null,
	constraint travel_code_pk
		primary key (travel_code)
);