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
(1, "VDR0000007686", 155900, 2020-03-12, 2020-03-21, 16, 16, "mm");