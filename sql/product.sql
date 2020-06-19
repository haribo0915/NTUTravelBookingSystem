create table product
(
	id int auto_increment,
	title varchar(200) not null,
	travel_code int not null,
	product_key varchar(200) not null,
	price int not null,
	start_date timestamp not null,
	end_date timestamp not null,
	lower_bound int null,
	upper_bound int null,
	FOREIGN KEY (travel_code) REFERENCES travel_code(travel_code)
		ON DELETE CASCADE
       		ON UPDATE CASCADE,
	constraint product_pk
		primary key (id)
);