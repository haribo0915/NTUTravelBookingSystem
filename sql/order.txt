create table `order`
(
	id int auto_increment,
	user_id int not null,
	product_id int not null,
	adult_count int not null,
	children_count int not null,
	total_price int not null,
	created_time timestamp not null,
	FOREIGN KEY (user_id) REFERENCES user(id),
	FOREIGN KEY (product_id) REFERENCES product(id),
	constraint order_pk
		primary key (id)
);