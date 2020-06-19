create table product
(
	id int auto_increment,
	travel_code_id int not null,
	product_key varchar(200) not null,
	price int not null,
	start_date timestamp not null,
	end_date timestamp not null,
	lower_bound int null,
	upper_bound int null,
	title varchar(200) not null,
	FOREIGN KEY (travel_code_id) REFERENCES travel_code(id),
	constraint product_pk
		primary key (id)
);